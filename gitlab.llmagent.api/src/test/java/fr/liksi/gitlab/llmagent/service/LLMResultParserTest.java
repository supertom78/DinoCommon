package fr.liksi.gitlab.llmagent.service;

import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@MockitoSettings
class LLMResultParserTest {

    private LLMResultParser llmResultParser = new LLMResultParser();

    @Test
    void when_result_contains_codemarkdown_then_return_codeinside() {
        final var result = """
        bla bla bla
        ```java
        public class Test {
            public static void main(String[] args) {
               System.out.println(Hello, World!);
            }
        }
        ```
        bla bla bla""";

        assertThat(llmResultParser.getParsedResult(result).orElse("")).isEqualTo("""
        public class Test {
            public static void main(String[] args) {
               System.out.println(Hello, World!);
            }
        }
        """);
    }

    @Test
    void when_result_contains_napasétéimpacté_then_return_nothing() {
        final var result = """
        Le fichier .gitlab-ci.yml n'a pas été impacté par le commit
        ```java
        public class Test {
            public static void main(String[] args) {
               System.out.println(Hello, World!);
            }
        }
        ```
        bla bla bla""";

        assertThat(llmResultParser.getParsedResult(result).isPresent()).isFalse();
    }

    @Test
    void when_result_contains_extract_thenfullfile__then_return_fullfile() {
        final var result = """
            Les différences du commit à propager sur le fichier truc.java sont les suivantes :

            ```java
    extract info fromfile;
    extract info fromfile;
    extract info fromfile;
```

    Cela signifie qu'il faut ajouter les lignes de code ci-dessus dans le fichier truc.java. Voici le code complet du fichier avec les modifications apportées :

            ```java
le vrai code à récupérer
```
""";

        assertThat(llmResultParser.getParsedResult(result).orElse("")).isEqualTo("""
    le vrai code à récupérer
    """);
    }




}