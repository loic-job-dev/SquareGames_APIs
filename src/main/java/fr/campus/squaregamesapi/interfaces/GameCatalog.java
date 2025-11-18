package fr.campus.squaregamesapi.interfaces;

import java.util.Collection;
import java.util.Locale;

public interface GameCatalog {
    String getGameIdentifiers();

    String getGamesRules(Locale locale);

    String getGameRules(Locale locale, String gameId);
}
