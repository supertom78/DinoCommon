package fr.liksi.gitlab.llmagent.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LLMResultParser {

    private static final Logger LOG = LoggerFactory.getLogger(LLMResultParser.class);

    final static String REGEX = "```\\S*\\s(.+)```";

    public Optional<String> getParsedResult(String resultToParse) {
        if(resultToParse.contains("code complet")){
            resultToParse = resultToParse.substring(resultToParse.indexOf("code complet"));
        }

        final Pattern pattern = Pattern.compile(REGEX, Pattern.DOTALL);
        final Matcher matcher = pattern.matcher(resultToParse);

        final var op = Optional.ofNullable(
                !resultToParse.toLowerCase().contains("n'a pas été impacté par le commit") && !resultToParse.toLowerCase().contains("no changes to propagate") && matcher.find() ? matcher.group(1) : null);

        LOG.debug("{} Parsed result: {}", resultToParse, op.orElse("No content found"));
        return op;
    }

}
