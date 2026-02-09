# 🕷️ ZG Hero | Web Crawler ANS (TISS)

> **Autor:** Levi  
> **Desafio:** ZG Hero - Web Scraping & Automação  
> **Status:** ✅ Concluído (Highlander Mode)

## 📖 Sobre o Projeto
Este projeto consiste em um **Bot/Web Crawler** desenvolvido em **Groovy** para automatizar a coleta de dados públicos do portal da **ANS (Agência Nacional de Saúde Suplementar)**.

O robô navega dinamicamente pelas páginas do Padrão TISS (Troca de Informação de Saúde Suplementar), identifica as versões mais recentes e realiza o download de arquivos técnicos, além de extrair dados históricos estruturados.

## 🚀 Funcionalidades (Tasks)

O Crawler foi desenhado para cumprir os 3 requisitos obrigatórios com robustez:

### 1️⃣ Download do Componente de Comunicação
- **O que faz:** Navega até a versão mais recente do Padrão TISS (ex: Jan/2026).
- **Inteligência:** Localiza e baixa o arquivo `.zip` contendo os esquemas (XSD) e WSDLs.
- **Arquivo gerado:** `Downloads_TISS/Componente_Comunicacao.zip`

### 2️⃣ Extração de Histórico de Versões
- **O que faz:** Acessa a página de histórico e varre a tabela de versões.
- **Filtro:** Exibe no console apenas as competências a partir de **Janeiro de 2016**.
- **Saída:** Dados estruturados (Competência | Publicação | Vigência) impressos no terminal.

### 3️⃣ Download da Tabela de Erros
- **O que faz:** Localiza a seção de "Tabelas Relacionadas" (ou equivalente).
- **Inteligência:** Identifica e baixa a planilha de erros de envio (`.xlsx`).
- **Arquivo gerado:** `Downloads_TISS/Tabela_Erros_ANS.xlsx`

---

## 🛠️ Tecnologias Utilizadas

* **Linguagem:** [Groovy](https://groovy-lang.org/) (JVM)
* **Gerenciador de Dependências:** [Gradle](https://gradle.org/)
* **Parsing & Navegação:** [Jsoup](https://jsoup.org/) (HTML Parser)
* **I/O:** Java NIO (para download eficiente de arquivos binários)

---

## 🧠 Diferenciais da Solução (Robustez)

Diferente de crawlers simples que buscam por textos exatos (ex: "Clique aqui"), este robô foi implementado com **estratégia baseada em URL e Regex**.

* ✅ **Ignora mudanças de Label:** Se o governo mudar o texto de "Histórico" para "Ver Histórico", o robô continua funcionando pois busca padrões na URL (`href`).
* ✅ **Busca Profunda:** Caso os links mudem de lugar na Home, o robô varre links relacionados para encontrar os arquivos.
* ✅ **User-Agent:** Configurado para simular um navegador real e evitar bloqueios.

---

## ⚙️ Como Executar

### Pré-requisitos
* Java JDK 11 ou superior (Recomendado JDK 17 ou 21).
* Git instalado.

### Passo a Passo

1. **Clone o repositório:**
   bash
   git clone [https://github.com/SEU_USUARIO/NOME_DO_REPO.git](https://github.com/SEU_USUARIO/NOME_DO_REPO.git)
   cd NOME_DO_REPO

2. Execute via Gradle:

Linux/Mac:

Bash
./gradlew run
Windows:

Bash
gradlew.bat run

3. Verifique os Resultados:

Acompanhe os logs no terminal.

Ao final, verifique a pasta Downloads_TISS/ criada na raiz do projeto.

📊 Exemplo de Saída (Console)
🚀 Iniciando Crawler (Modo Busca Profunda)...

--- 🕵️ [TASK 1] Verificando versão mais recente... ---
📅 Versão encontrada (URL): .../padrao-tiss-janeiro-2026
⬇️ Baixando Componente...
   -> Sucesso: Componente_Comunicacao.zip salvo!

--- 📜 [TASK 2] Histórico de Versões... ---
🔎 Entrando no Histórico...
COMPETÊNCIA          | PUBLICAÇÃO      | VIGÊNCIA       
------------------------------------------------------------
Fev/2024             | 15/02/2024      | 01/03/2024     
Jan/2024             | 15/01/2024      | 01/02/2024     
... (lista completa)

--- ⚠️ [TASK 3] Buscando Tabela de Erros... ---
⬇️ Encontrado: .../Tabelaerrosenvioparaanspadraotiss.xlsx
   -> Sucesso: Tabela_Erros_ANS.xlsx salvo!

✅ FIM DO PROCESSO! Confira a pasta Downloads_TISS

Desenvolvido para o desafio ZG Soluções - Hero Project.
