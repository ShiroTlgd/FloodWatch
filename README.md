# FloodWatch

Aplicativo Android para cadastro e acompanhamento colaborativo de áreas suscetíveis a alagamentos, com consulta de chuva em tempo real para cada ponto cadastrado.

Desenvolvido em **Kotlin** com **Jetpack Compose**, seguindo a arquitetura **MVVM**.

## Descrição da solução

Em muitas cidades brasileiras, os alagamentos se repetem nos mesmos pontos a cada temporada de chuvas, e a informação sobre esses locais costuma ficar espalhada ou perdida.

O FloodWatch permite que o usuário cadastre as regiões que costumam alagar, informando nome, endereço, coordenadas, volume de chuva, reincidências, profundidade estimada da água, nível de risco e status atual. O app organiza essas áreas em um painel com resumo, busca e filtro por nível de risco, e permite favoritar as áreas mais importantes.

Para cada área, o app consulta a previsão de chuva da API Open-Meteo usando a latitude e a longitude cadastradas, ajudando o usuário a se antecipar a novos episódios de alagamento.

## Tema da Global Solution

O FloodWatch trata da **prevenção e resposta a eventos climáticos extremos**, com foco em cidades mais resilientes. Ao reunir em um só lugar os pontos de alagamento, seus níveis de risco e a previsão de chuva em cada um, o app apoia a tomada de decisão de moradores e da comunidade antes e durante os períodos de chuva intensa.

## Fluxo de telas

```
Splash ──(já marcou "não mostrar novamente")──────────────┐
   │                                                       ▼
   └──(primeira vez / não marcou)──> Introdução (3 páginas) ──> Início ◄──► Favoritos ◄──► Clima
                                                              │  │              │
                                              (botão +)       │  └──> Detalhes <┘
                                                              ▼          │  │
                                                    Cadastrar área   Atualizar / Excluir
```

| Tela | Função |
|------|--------|
| Splash | Exibe o logo e faz a transição automática; consulta o SharedPreferences para decidir o próximo destino |
| Introdução | 3 páginas com imagem, título e descrição; botões Voltar e Avançar; opção "Não mostrar esta introdução novamente" |
| Início | Resumo das áreas, busca por nome ou endereço, filtro por risco, lista de áreas e botão de cadastro |
| Favoritos | Áreas marcadas com o coração |
| Clima | Escolha de uma área cadastrada e consulta à API pelas coordenadas dela |
| Detalhes | Dados da área, clima no ponto, favoritar, atualizar e excluir (com confirmação) |
| Cadastro / Atualização | Formulário com validação dos campos |

## API utilizada

[Open-Meteo](https://open-meteo.com/en/docs): API pública e gratuita, sem necessidade de chave. Endpoint utilizado: `GET /v1/forecast`.

- `current=precipitation,rain`: chuva no momento
- `hourly=precipitation_probability,precipitation`: probabilidade e volume de chuva por hora
- `forecast_days=2` e `timezone=auto`

O consumo é feito com **Retrofit + Gson**, usando a latitude e a longitude da área escolhida. A tela trata os quatro estados da consulta:

| Estado | Comportamento |
|--------|---------------|
| Inicial | Orienta o usuário a tocar em uma área para buscar os dados |
| Carregando | Indicador de progresso com mensagem |
| Sucesso | Precipitação atual, alerta e probabilidade de chuva nas próximas 6 horas |
| Erro | Mensagem amigável e botão "Tentar novamente" |

## Arquitetura

O projeto segue **MVVM** com camadas separadas:

```
br.com.fiap.rm98825
├── data
│   ├── local        Room (AreaDao, FloodDatabase) e SharedPreferences (UserPreferences)
│   ├── remote       Retrofit (WeatherApi) e DTOs da resposta
│   └── repository   AreaRepository: única fonte de dados para os ViewModels
├── domain           AreaForm e AreaValidator (regras de validação do formulário)
├── model            AreaEntity, WeatherInfo e constantes de risco e status
├── viewmodel        AreaViewModel (lista, busca, filtro, favoritos, CRUD)
│                    WeatherViewModel + WeatherUiState (Idle / Loading / Success / Error)
├── navigation       Routes e AppNavigation (Navigation Compose)
└── ui
    ├── theme        Cores e tema Material 3
    ├── components   Composables reutilizáveis (scaffolds, cards, painel de clima, campos)
    └── screens      Uma tela por arquivo, sem regra de negócio
```

- **Estado:** as telas apenas observam `StateFlow` dos ViewModels (`collectAsState`) e enviam eventos.
- **Regra de negócio fora da tela:** validação em `AreaValidator`; busca, filtro e contagens em `AreaViewModel`; carregamento e tratamento de erro em `WeatherViewModel`.
- **Reutilização:** `MainScaffold`, `DetailScaffold`, `AreaCard`, `WeatherPanel`, `EmptyState`, `ChipSelector`, `FormTextField` e `RiskBadge` são usados em várias telas.
- **Persistência:** o Room guarda as áreas cadastradas e o SharedPreferences guarda a preferência da introdução.

## Tecnologias

- Kotlin 2.0.21 e Jetpack Compose (Material 3)
- Navigation Compose
- Room (com KSP)
- Retrofit, Gson e OkHttp
- Coroutines e StateFlow
- Min SDK 26 / Target SDK 35

## Como executar

1. Extraia o arquivo ZIP.
2. Abra a pasta `FloodWatch` no Android Studio.
3. Aguarde a sincronização do Gradle.
4. Execute em um emulador ou dispositivo Android com acesso à internet.

> Se uma versão anterior do app já estiver instalada, desinstale-a antes de executar, pois o banco de dados foi atualizado.

## Como testar a consulta de clima

Cadastre uma área com as coordenadas de um local onde esteja chovendo e toque nela na aba **Clima**. Exemplo: Singapura (`1.3521`, `103.8198`), onde chove com frequência. Os valores mudam ao longo do dia.

## Desenvolvedores

> **Preencher:** nome e RM de cada integrante do grupo.
