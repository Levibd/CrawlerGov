import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.regex.Matcher
import java.util.regex.Pattern

/*
 * Autor: Levi (Highlander)
 * Projeto: Web Crawler ANS - Versão Final (URL Based)
 */
class AnsCrawler {

    static final String BASE_URL = "https://www.gov.br/ans/pt-br"
    static final String START_URL = "https://www.gov.br/ans/pt-br/assuntos/prestadores/padrao-para-troca-de-informacao-de-saude-suplementar-2013-tiss"
    static final String PASTA_DOWNLOADS = "Downloads_TISS"

    static void main(String[] args) {
        println "🚀 Iniciando Crawler (Modo Sniper de URL)..."

        File diretorio = new File(PASTA_DOWNLOADS)
        if (!diretorio.exists()) diretorio.mkdirs()

        try {
            Document docPrincipal = conectar(START_URL)

            // ===================================================================================
            // TASK 1: COMPONENTE (JÁ FUNCIONA)
            // ===================================================================================
            println "\n--- 🕵️ [TASK 1] Baixando Componente... ---"
            Element linkVersaoAtual = docPrincipal.select("a").find {
                String href = it.attr("href")
                href.contains("padrao-tiss") && href.matches(".*20[2-9][0-9].*") && !href.contains("historico")
            }

            if (linkVersaoAtual) {
                Document docVersao = conectar(linkVersaoAtual.attr("href"))
                Element linkComponente = docVersao.select("a").find {
                    it.text().toLowerCase().contains("componente") && it.text().toLowerCase().contains("comunicação")
                }
                if (linkComponente) baixarArquivo(linkComponente.attr("href"), "Componente_Comunicacao.zip")
            }

            // ===================================================================================
            // TASK 2: HISTÓRICO (BUSCA PELA URL)
            // ===================================================================================
            println "\n--- 📜 [TASK 2] Histórico de Versões... ---"

            
            Element linkHistorico = docPrincipal.select("a").find {
                it.attr("href").contains("historico-das-versoes")
            }

            if (linkHistorico) {
                Document docHistorico = conectar(linkHistorico.attr("href"))
                def linhas = docHistorico.select("table tr")
                if (linhas.isEmpty()) linhas = docHistorico.select("tbody tr")

                if (!linhas.isEmpty()) {
                    println String.format("%-20s | %-15s | %-15s", "COMPETÊNCIA", "PUBLICAÇÃO", "VIGÊNCIA")
                    println "-" * 60

                    linhas.each { tr ->
                        def colunas = tr.select("td")
                        if (colunas.size() >= 3) {
                            String textoRaw = colunas[0].text().trim()

                            def matcher = (textoRaw =~ /20\d{2}/)
                            if (matcher.find()) {
                                int ano = matcher.group().toInteger()
                                if (ano >= 2016) {
                                    println String.format("%-20s | %-15s | %-15s",
                                            textoRaw, colunas[1].text().trim(), colunas[2].text().trim())
                                }
                            }
                        }
                    }
                } else {
                    println "❌ Tabela não encontrada dentro da página de histórico."
                }
            } else {
                println "❌ Link para Histórico não encontrado (URL mudou?)."
            }

            // ===================================================================================
            // TASK 3: TABELA DE ERROS
            // ===================================================================================
            println "\n--- ⚠️ [TASK 3] Buscando Tabela de Erros... ---"


            Element linkPaginaTabelas = docPrincipal.select("a").find {
                it.attr("href").contains("tabelas-relacionadas")
            }

            if (linkPaginaTabelas) {
                println "🔎 Entrando na página de tabelas: ${linkPaginaTabelas.attr("href")}"
                Document docTabelas = conectar(linkPaginaTabelas.attr("href"))


                Element linkArquivoErro = docTabelas.select("a").find {
                    String href = it.attr("href").toLowerCase()
                    href.endsWith(".xlsx") && href.contains("erro")
                }

                if (linkArquivoErro) {
                    println "⬇️ Encontrado: ${linkArquivoErro.attr("href")}"
                    baixarArquivo(linkArquivoErro.attr("href"), "Tabela_Erros_ANS.xlsx")
                } else {
                    println "❌ Arquivo .xlsx de erro não encontrado na página de tabelas."
                }
            } else {
                println "❌ Página 'Tabelas Relacionadas' não encontrada (URL mudou?)."
            }

            println "\n✅ FIM DO PROCESSO! Confira a pasta $PASTA_DOWNLOADS"

        } catch (Exception e) {
            println "🔥 Erro Fatal: " + e.message
            e.printStackTrace()
        }
    }


    static Document conectar(String url) {
        if (!url.startsWith("http")) url = BASE_URL + url
        return Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                .timeout(60000)
                .ignoreContentType(true)
                .get()
    }

    static void baixarArquivo(String url, String nomeSalvar) {
        if (!url.startsWith("http")) url = BASE_URL + url
        try (InputStream inStream = new URL(url).openStream()) {
            Files.copy(inStream, Paths.get(PASTA_DOWNLOADS, nomeSalvar), StandardCopyOption.REPLACE_EXISTING)
            println "   -> Sucesso: Arquivo salvo como $nomeSalvar"
        } catch (Exception e) {
            println "   -> Falha ao baixar: ${e.message}"
        }
    }
}