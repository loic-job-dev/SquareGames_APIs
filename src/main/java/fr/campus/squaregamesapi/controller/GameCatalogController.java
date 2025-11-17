package fr.campus.squaregamesapi.controller;

import fr.campus.squaregamesapi.interfaces.GameCatalog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameCatalogController {

    @Autowired
    private GameCatalog gameCatalog;

    @GetMapping("/games")
    public String gamecatalog() {
        return gameCatalog.getGameRules(LocaleContextHolder.getLocale());
    }

}
