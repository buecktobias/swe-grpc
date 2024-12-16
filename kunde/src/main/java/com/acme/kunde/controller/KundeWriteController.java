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

import com.acme.kunde.controller.KundeDTO.OnCreate;
import com.acme.kunde.service.EmailExistsException;
import com.acme.kunde.service.KundeWriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import java.net.URI;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import static com.acme.kunde.controller.KundeGetController.API_PATH;
import static com.acme.kunde.controller.KundeGetController.ID_PATTERN;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.created;

/// Eine Controller-Klasse bildet die REST-Schnittstelle, wobei die HTTP-Methoden, Pfade und MIME-Typen auf die
/// Methoden der Klasse abgebildet werden.
/// ![Klassendiagramm](../../../../../asciidoc/KundeWriteController.svg)
///
/// [Jürgen Zimmermann](mailto:Juergen.Zimmermann@h-ka.de)
// Maven: ![Klassendiagramm](../../../../../../generated-docs/KundeWriteController.svg)
@Controller
@Validated
@RequestMapping(API_PATH)
@SuppressWarnings({"ClassFanOutComplexity", "java:S1075"})
class KundeWriteController {
    private static final Logger LOGGER = LoggerFactory.getLogger(KundeWriteController.class);
    private static final String PROBLEM_PATH = "/problem/";

    private final KundeWriteService service;
    private final KundeMapper mapper;
    private final UriHelper uriHelper;

    /// Konstruktor mit _package private_ für _Spring_.
    ///
    /// @param service Injiziertes Service-Objekt.
    /// @param mapper Injiziertes Mapper-Objekt für das Mapping von DTO-Objekten auf Entity-Objekte.
    /// @param uriHelper Injiziertes Helper-Objekt, um URIs für z.B. den Header _Location_ zu bauen.
    KundeWriteController(final KundeWriteService service, final KundeMapper mapper, final UriHelper uriHelper) {
        this.service = service;
        this.mapper = mapper;
        this.uriHelper = uriHelper;
    }

    /// Einen neuen Kunde-Datensatz anlegen.
    ///
    /// @param kundeDTO Das Kundeobjekt aus dem eingegangenen Request-Body.
    /// @param request Das Request-Objekt, um `Location` im Response-Header zu erstellen.
    /// @return Response mit Statuscode `201` einschließlich Location-Header oder Statuscode `422`, falls Constraints
    /// verletzt sind oder die Emailadresse bereits existiert oder Statuscode `400`, falls syntaktische Fehler im
    /// Request-Body vorliegen.
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Einen neuen Kunden anlegen", tags = "Neuanlegen")
    @ApiResponse(responseCode = "201", description = "Kunde neu angelegt")
    @ApiResponse(responseCode = "400", description = "Syntaktische Fehler im Request-Body")
    @ApiResponse(responseCode = "422", description = "Ungültige Werte oder Email vorhanden")
    ResponseEntity<Void> post(
        @RequestBody @Validated({Default.class, OnCreate.class}) final KundeDTO kundeDTO,
        final HttpServletRequest request
    ) {
        LOGGER.debug("post: {}", kundeDTO);

        final var kundeInput = mapper.toKunde(kundeDTO);
        final var kunde = service.create(kundeInput);
        final var baseUri = uriHelper.getBaseUri(request).toString();
        final var location = URI.create(baseUri + '/' + kunde.getId());
        return created(location).build();
    }

    /// Einen vorhandenen Kunde-Datensatz überschreiben.
    ///
    /// @param id ID des zu aktualisierenden Kunden.
    /// @param kundeDTO Das Kundenobjekt aus dem eingegangenen Request-Body.
    @PutMapping(path = "{id:" + ID_PATTERN + "}", consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Einen Kunden mit neuen Werten aktualisieren", tags = "Aktualisieren")
    @ApiResponse(responseCode = "204", description = "Aktualisiert")
    @ApiResponse(responseCode = "400", description = "Syntaktische Fehler im Request-Body")
    @ApiResponse(responseCode = "404", description = "Kunde nicht vorhanden")
    @ApiResponse(responseCode = "422", description = "Ungültige Werte oder Email vorhanden")
    void put(
        @PathVariable final UUID id,
        @RequestBody @Valid final KundeDTO kundeDTO
    ) {
        LOGGER.debug("put: id={}, {}", id, kundeDTO);
        final var kundeInput = mapper.toKunde(kundeDTO);
        service.update(kundeInput, id);
    }

    /// Einen vorhandenen Kunden anhand seiner ID löschen.
    ///
    /// @param id ID des zu löschenden Kunden.
    @DeleteMapping(path = "{id:" + ID_PATTERN + "}")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Einen Kunden anhand der ID loeschen", tags = "Loeschen")
    @ApiResponse(responseCode = "204", description = "Gelöscht")
    void deleteById(@PathVariable final UUID id)  {
        LOGGER.debug("deleteById: id={}", id);
        service.deleteById(id);
    }

    /// [ExceptionHandler] für [MethodArgumentNotValidException]
    ///
    /// @param ex Exception für Fehler im Request-Body bei `POST` oder `PUT` gemäß _Jakarta Validation_.
    /// @param request Injiziertes Request-Objekt gemäß _Jakarta Servlets_.
    /// @return Objekt mit `ProblemDetail` gemäß _RFC 9457_.
    @ExceptionHandler
    ProblemDetail onConstraintViolations(
        final MethodArgumentNotValidException ex,
        final HttpServletRequest request
    ) {
        LOGGER.debug("onConstraintViolations: {}", ex.getMessage());

        final var detailMessages = ex.getDetailMessageArguments();
        final var detail = detailMessages == null
            ? "Constraint Violations"
            : ((String) detailMessages[1]).replace(", and ", ", ");
        final var problemDetail = ProblemDetail.forStatusAndDetail(UNPROCESSABLE_ENTITY, detail);
        problemDetail.setType(URI.create(PROBLEM_PATH + ProblemType.CONSTRAINTS.getValue()));
        problemDetail.setInstance(URI.create(request.getRequestURL().toString()));

        return problemDetail;
    }

    /// [ExceptionHandler] für [EmailExistsException]
    ///
    /// @param ex Exception für die fehlerhafte Emailadresse.
    /// @param request Injiziertes Request-Objekt gemäß _Jakarta Servlets_.
    /// @return Objekt mit `ProblemDetail` gemäß _RFC 9457_.
    @ExceptionHandler
    ProblemDetail onEmailExists(final EmailExistsException ex, final HttpServletRequest request) {
        LOGGER.debug("onEmailExists: {}", ex.getMessage());
        final var problemDetail = ProblemDetail.forStatusAndDetail(UNPROCESSABLE_ENTITY, ex.getMessage());
        problemDetail.setType(URI.create(PROBLEM_PATH + ProblemType.CONSTRAINTS.getValue()));
        problemDetail.setInstance(URI.create(request.getRequestURL().toString()));
        return problemDetail;
    }

    /// [ExceptionHandler] für [HttpMessageNotReadableException]
    ///
    /// @param ex Exception für den syntaktisch falschen Request-Body bei `POST` oder `PUT`.
    /// @param request Injiziertes Request-Objekt gemäß _Jakarta Servlets_.
    /// @return Objekt mit `ProblemDetail` gemäß _RFC 9457_.
    @ExceptionHandler
    ProblemDetail onMessageNotReadable(
        final HttpMessageNotReadableException ex,
        final HttpServletRequest request
    ) {
        LOGGER.debug("onMessageNotReadable: {}", ex.getMessage());
        final var problemDetail = ProblemDetail.forStatusAndDetail(BAD_REQUEST, ex.getMessage());
        problemDetail.setType(URI.create(PROBLEM_PATH + ProblemType.BAD_REQUEST.getValue()));
        problemDetail.setInstance(URI.create(request.getRequestURL().toString()));
        return problemDetail;
    }
}
