package fr.campus.squaregamesapi.interfaces;

import java.util.Collection;
import java.util.Locale;

public interface GameCatalog {
    String getGameIdentifiers();

    String getGameRules(Locale locale);
}
