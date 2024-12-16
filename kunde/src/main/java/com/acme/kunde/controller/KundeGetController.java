/*
 * Copyright (C) 2022 - present Juergen Zimmermann, Hochschule Karlsruhe
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.acme.kunde.controller;

import com.acme.kunde.service.KundeReadService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.lang.NonNull;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import static com.acme.kunde.controller.KundeGetController.API_PATH;
import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/// Eine Controller-Klasse bildet die REST-Schnittstelle, wobei die HTTP-Methoden, Pfade und MIME-Typen auf die
/// Methoden der Klasse abgebildet werden.
/// ![Klassendiagramm](../../../../../asciidoc/KundeGetController.svg)
///
/// @author [Jürgen Zimmermann](mailto:Juergen.Zimmermann@h-ka.de)
// Maven: ![Klassendiagramm](../../../../../../generated-docs/KundeGetController.svg)
@RestController
@RequestMapping(API_PATH)
@OpenAPIDefinition(info = @Info(title = "Kunde API", version = "v1"))
@SuppressWarnings("java:S1075")
public class KundeGetController {
    /// Basispfad für die REST-Schnittstelle.
    public static final String API_PATH = "/api";

    /// Pfad, um Nachnamen abzufragen.
    public static final String NACHNAME_PATH = "/nachname";

    /// Muster für eine UUID.
    public static final String ID_PATTERN = "[\\da-f]{8}-[\\da-f]{4}-[\\da-f]{4}-[\\da-f]{4}-[\\da-f]{12}";

    private static final Logger LOGGER = LoggerFactory.getLogger(KundeGetController.class);

    /// Pfad, um Nachnamen abzufragen.
    private final KundeReadService service;
    private final UriHelper uriHelper;

    /// Konstruktor mit _package private_ für _Spring_.
    ///
    /// @param service Injiziertes Service-Objekt.
    /// @param uriHelper Injiziertes Helper-Objekt, um URIs für z.B. _Spring HATEOAS_ zu bauen.
    KundeGetController(final KundeReadService service, final UriHelper uriHelper) {
        this.service = service;
        this.uriHelper = uriHelper;
    }

    // https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html#webflux-ann-methods
    // https://localhost:8080/swagger-ui.html
    /// Suche anhand der Kunde-ID als Pfad-Parameter.
    ///
    /// @param id ID des zu suchenden Kunden
    /// @param request Das Request-Objekt, um Links für _HATEOAS_ zu erstellen.
    /// @return Gefundener Kunde mit _Atom-Links_.
    @GetMapping(path = "{id:" + ID_PATTERN + "}", produces = {HAL_JSON_VALUE, APPLICATION_JSON_VALUE})
    @Operation(summary = "Suche mit der Kunde-ID", tags = "Suchen")
    @ApiResponse(responseCode = "200", description = "Kunde gefunden")
    @ApiResponse(responseCode = "404", description = "Kunde nicht gefunden")
    KundeModel getById(@PathVariable final UUID id, final HttpServletRequest request) {
        LOGGER.debug("getById: id={}, Thread={}", id, Thread.currentThread().getName());

        // Geschaeftslogik bzw. Anwendungskern
        final var kunde = service.findById(id);

        // HATEOAS
        final var model = new KundeModel(kunde);
        // evtl. Forwarding von einem API-Gateway
        final var baseUri = uriHelper.getBaseUri(request).toString();
        final var idUri = baseUri + '/' + kunde.getId();
        final var selfLink = Link.of(idUri);
        final var listLink = Link.of(baseUri, LinkRelation.of("list"));
        final var addLink = Link.of(baseUri, LinkRelation.of("add"));
        final var updateLink = Link.of(idUri, LinkRelation.of("update"));
        final var removeLink = Link.of(idUri, LinkRelation.of("remove"));
        model.add(selfLink, listLink, addLink, updateLink, removeLink);

        LOGGER.debug("getById: {}", model);
        return model;
    }

    /// Suche mit diversen Suchkriterien als Query-Parameter.
    ///
    /// @param suchkriterien Query-Parameter als Map .
    /// @param request Das Request-Objekt, um Links für _HATEOAS_ zu erstellen.
    /// @return Gefundenen Kunden als [CollectionModel].
    @GetMapping(produces = {HAL_JSON_VALUE, APPLICATION_JSON_VALUE})
    @Operation(summary = "Suche mit Suchkriterien", tags = "Suchen")
    @ApiResponse(responseCode = "200", description = "CollectionModel mid den Kunden")
    @ApiResponse(responseCode = "404", description = "Keine Kunden gefunden")
    CollectionModel<KundeModel> get(
        @RequestParam @NonNull final MultiValueMap<String, String> suchkriterien,
        final HttpServletRequest request
    ) {
        LOGGER.debug("get: suchkriterien={}", suchkriterien);

        final var baseUri = uriHelper.getBaseUri(request).toString();

        // Geschaeftslogik bzw. Anwendungskern
        final var models = service.find(suchkriterien)
            .parallelStream()
            .map(kunde -> {
                final var model = new KundeModel(kunde);
                model.add(Link.of(baseUri + '/' + kunde.getId()));
                return model;
            })
            .toList();

        LOGGER.debug("get: {}", models);
        return CollectionModel.of(models);
    }

    /// Abfrage, welche Nachnamen es zu einem Präfix gibt.
    ///
    /// @param prefix Nachname-Präfix als Pfadvariable.
    /// @return Die passenden Nachnamen oder Statuscode `404`, falls es keine gibt.
    @GetMapping(path = NACHNAME_PATH + "/{prefix}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Suche Nachnamen mit Praefix", tags = "Suchen")
    String getNachnamenByPrefix(@PathVariable final String prefix) {
        LOGGER.debug("getNachnamenByPrefix: {}", prefix);
        final var nachnamen = service.findNachnamenByPrefix(prefix);
        LOGGER.debug("getNachnamenByPrefix: {}", nachnamen);
        return nachnamen.parallelStream()
            .map(nachname -> "\"" + nachname + '"')
            .toList()
            .toString();
    }
}
