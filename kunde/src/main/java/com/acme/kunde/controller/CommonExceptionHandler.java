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

import com.acme.kunde.service.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/// Handler für allgemeine Exceptions.
///
/// @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
@ControllerAdvice
class CommonExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonExceptionHandler.class);

    /// Konstruktor mit _package private_ für _Spring_.
    CommonExceptionHandler() {
    }

    /// [ExceptionHandler], wenn ein Kunde gesucht wird, aber nicht vorhanden ist.
    ///
    /// @param ex Die zugehörige [NotFoundException].
    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    void onNotFound(final NotFoundException ex) {
        LOGGER.debug("onNotFound: {}", ex.getMessage());
    }
}
