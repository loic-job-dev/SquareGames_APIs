package fr.campus.squaregamesapi.controller;

import fr.campus.squaregamesapi.interfaces.GameCatalog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameCatalogController {

    @Autowired
    private GameCatalog gameCatalog;

    @Operation(
            summary = "Get all games rules",
            description = "Retrieve the rules of all games available in the catalog",
            tags = {"Game", "Catalog"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of games rules returned successfully", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/games")
    public String gamecatalog() {
        return gameCatalog.getGamesRules(LocaleContextHolder.getLocale());
    }

    @Operation(
            summary = "Get a specific game's rules",
            description = "Retrieve the rules for a specific game by its ID",
            tags = {"Game", "Catalog"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Game rules returned successfully", content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "404", description = "Game not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/games/{gameId}")
    public Object getGame(
            @Parameter(description = "ID of the game to retrieve", required = true)
            @PathVariable("gameId") String gameId
    ) {
        return gameCatalog.getGameRules(LocaleContextHolder.getLocale(), gameId);
    }
}
