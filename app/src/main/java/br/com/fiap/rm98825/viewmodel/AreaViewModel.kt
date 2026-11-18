package br.com.fiap.rm98825.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.rm98825.data.repository.AreaRepository
import br.com.fiap.rm98825.domain.AreaForm
import br.com.fiap.rm98825.domain.AreaValidator
import br.com.fiap.rm98825.domain.FormResult
import br.com.fiap.rm98825.model.AreaEntity
import br.com.fiap.rm98825.model.AreaStatus
import br.com.fiap.rm98825.model.AreaSummary
import br.com.fiap.rm98825.model.RiskLevel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AreaViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AreaRepository.create(application)

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _riskFilter = MutableStateFlow<String?>(null)
    val riskFilter: StateFlow<String?> = _riskFilter.asStateFlow()

    val allAreas: StateFlow<List<AreaEntity>> = repository.areas.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val visibleAreas: StateFlow<List<AreaEntity>> =
        combine(allAreas, _query, _riskFilter) { areas, text, risk ->
            areas.filter { area ->
                (risk == null || area.risk == risk) &&
                    (text.isBlank() ||
                        area.name.contains(text, ignoreCase = true) ||
                        area.address.contains(text, ignoreCase = true))
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favorites: StateFlow<List<AreaEntity>> =
        allAreas.map { areas -> areas.filter { it.isFavorite } }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val summary: StateFlow<AreaSummary> =
        allAreas.map { areas ->
            AreaSummary(
                total = areas.size,
                highRisk = areas.count { RiskLevel.isHigh(it.risk) },
                flooded = areas.count { it.status == AreaStatus.FLOODED }
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AreaSummary())

    fun onQueryChange(text: String) {
        _query.value = text
    }

    fun onRiskFilterChange(risk: String?) {
        _riskFilter.value = risk
    }

    fun observeArea(id: Int): Flow<AreaEntity?> = repository.observeArea(id)

    suspend fun getArea(id: Int): AreaEntity? = repository.getArea(id)

    fun toggleFavorite(area: AreaEntity) {
        viewModelScope.launch { repository.setFavorite(area.id, !area.isFavorite) }
    }

    fun delete(area: AreaEntity) {
        viewModelScope.launch { repository.delete(area) }
    }

    fun submit(editingId: Int?, form: AreaForm): String? {
        return when (val result = AreaValidator.validate(form)) {
            is FormResult.Invalid -> result.message
            is FormResult.Valid -> {
                viewModelScope.launch {
                    val existing = editingId?.let { repository.getArea(it) }
                    repository.save(
                        result.area.copy(
                            id = existing?.id ?: 0,
                            isFavorite = existing?.isFavorite ?: false
                        )
                    )
                }
                null
            }
        }
    }
}
