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
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.acme.kunde.controller;

import com.acme.kunde.entity.InteresseType;
import com.jayway.jsonpath.JsonPath;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.http.HttpClient;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.gaul.modernizer_maven_annotations.SuppressModernizer;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.hateoas.mediatype.hal.HalLinkDiscoverer;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.util.UriComponentsBuilder;
import static com.acme.kunde.controller.KundeGetController.API_PATH;
import static com.acme.kunde.dev.DevConfig.DEV;
import static com.acme.kunde.entity.Kunde.NACHNAME_PATTERN;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowableOfType;
import static org.junit.jupiter.api.condition.JRE.JAVA_23;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Tag("integration")
@Tag("rest")
@Tag("rest-get")
@DisplayName("REST-Schnittstelle fuer GET-Requests testen")
@ExtendWith(SoftAssertionsExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles(DEV)
@EnabledForJreRange(min = JAVA_23, max = JAVA_23)
@SuppressWarnings({"WriteTag", "ClassFanOutComplexity", "MissingJavadoc", "MissingJavadocType", "JavadocVariable"})
public class KundeGetControllerTest {
    public static final ClientHttpRequestFactory REQUEST_FACTORY;
    public static final String SCHEMA = "https";
    public static final String HOST = "localhost";

    private static final int READ_TIMEOUT_IN_MILLIS = 2_000;

    private static final String ID_VORHANDEN = "00000000-0000-0000-0000-000000000001";
    private static final String ID_NICHT_VORHANDEN = "ffffffff-ffff-ffff-ffff-ffffffffffff";
    private static final String NACHNAME = "Alpha";

    private static final String NACHNAME_PARAM = "nachname";
    private static final String INTERESSE_PARAM = "interesse";
    private static final String LESEN = "L";
    private static final String REISEN = "R";

    private static final long TIMEOUT_IN_SECONDS = 10;

    private final String baseUrl;
    private final KundeRepository kundeRepo;

    @InjectSoftAssertions
    private SoftAssertions softly;

    static {
        final var path = Paths.get("src", "main", "resources", "certificate.crt");
        // https://stackoverflow.com/questions/50025086/...
        //              ...in-java-what-is-the-simplest-way-to-create-an-sslcontext-with-just-a-pem-file#answer-61680878
        final SSLContext sslContext;
        try (var certificateStream = Files.lines(path)) {
            final var certificateBytes = certificateStream.collect(Collectors.joining("\n")).getBytes(UTF_8);
            // https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/security/cert/CertificateFactory.html
            final var certificateFactory = CertificateFactory.getInstance("X.509");
            final var certificate = certificateFactory.generateCertificate(new ByteArrayInputStream(certificateBytes));
            final var truststore = KeyStore.getInstance(KeyStore.getDefaultType());
            truststore.load(null, null);
            truststore.setCertificateEntry("microservice", certificate);
            // "X509"
            final var trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(truststore);
            sslContext = SSLContext.getInstance("TLSv1.3");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
        } catch (IOException | CertificateException | KeyStoreException | NoSuchAlgorithmException |
                 KeyManagementException e) {
            throw new IllegalStateException(e);
        }

        final var httpClient = HttpClient.newBuilder()
            .sslContext(sslContext)
            .connectTimeout(Duration.ofSeconds(TIMEOUT_IN_SECONDS))
            .build();
        final var jdkRequestFactory = new JdkClientHttpRequestFactory(httpClient);
        jdkRequestFactory.setReadTimeout(READ_TIMEOUT_IN_MILLIS);
        REQUEST_FACTORY = jdkRequestFactory;
    }

    @SuppressFBWarnings("CT")
    KundeGetControllerTest(@LocalServerPort final int port, final ApplicationContext ctx) {
        final var getController = ctx.getBean(KundeGetController.class);
        assertThat(getController).isNotNull();

        final var uriComponents = UriComponentsBuilder.newInstance()
            .scheme(SCHEMA)
            .host(HOST)
            .port(port)
            .path(API_PATH)
            .build();
        baseUrl = uriComponents.toUriString();

        // https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#io.rest-client.webclient.ssl
        // siehe org.springframework.web.client.DefaultRestClient
        final var restClient = RestClient
            .builder()
            .requestFactory(REQUEST_FACTORY)
            .baseUrl(baseUrl)
            .build();
        final var clientAdapter = RestClientAdapter.create(restClient);
        final var proxyFactory = HttpServiceProxyFactory.builderFor(clientAdapter).build();
        kundeRepo = proxyFactory.createClient(KundeRepository.class);
    }

    @Test
    @DisplayName("Immer erfolgreich")
    void immerErfolgreich() {
        assertThat(true).isTrue(); // NOSONAR
    }

    @Test
    @DisplayName("Noch nicht fertig")
    @Disabled("Nur zur Demo")
    void nochNichtFertig() {
        //noinspection DataFlowIssue
        assertThat(false).isTrue(); // NOSONAR
    }

    @Test
    @DisplayName("Suche nach allen Kunden")
    void findAll() {
        // given
        final MultiValueMap<String, String> suchkriterien = MultiValueMap.fromSingleValue(Map.of());

        // when
        final var kunden = kundeRepo.get(suchkriterien);

        // then
        softly.assertThat(kunden).isNotNull();
        final var embedded = kunden._embedded();
        softly.assertThat(embedded).isNotNull();
        final var embeddedKunden = embedded.kunden();
        softly.assertThat(embeddedKunden)
            .isNotNull()
            .isNotEmpty();
    }

    @ParameterizedTest(name = "[{index}] Suche mit vorhandenem Nachnamen: nachname={0}")
    @ValueSource(strings = NACHNAME)
    @DisplayName("Suche mit vorhandenem Nachnamen")
    void getByNachname(final String nachname) {
        // given
        final var suchkriterien = MultiValueMap.fromSingleValue(Map.of(NACHNAME_PARAM, nachname));

        // when
        final var kunden = kundeRepo.get(suchkriterien);

        // then
        softly.assertThat(kunden).isNotNull();
        final var embedded = kunden._embedded();
        softly.assertThat(embedded).isNotNull();
        final var kundenList = embedded.kunden();
        softly.assertThat(kundenList)
            .isNotNull()
            .isNotEmpty();
        kundenList
            .stream()
            .map(KundeDownload::nachname)
            .forEach(nachnameTmp -> softly.assertThat(nachnameTmp).isEqualTo(nachname));
    }

    @ParameterizedTest(name = "[{index}] Suche mit einem Interesse: interesse={0}")
    @ValueSource(strings = {LESEN, REISEN})
    @DisplayName("Suche mit einem Interesse")
    void getByInteresse(final String interesseStr) {
        // given
        final var suchkriterien = MultiValueMap.fromSingleValue(Map.of(INTERESSE_PARAM, interesseStr));

        // when
        final var kunden = kundeRepo.get(suchkriterien);

        // then
        assertThat(kunden).isNotNull();
        final var embedded = kunden._embedded();
        softly.assertThat(embedded).isNotNull();
        final var kundenList = embedded.kunden();
        softly.assertThat(kundenList)
            .isNotNull()
            .isNotEmpty();
        final var interesse = InteresseType.of(interesseStr);
        kundenList.forEach(kunde -> {
            final var interessen = kunde.interessen();
            softly.assertThat(interessen)
                .isNotNull()
                .doesNotContainNull()
                .contains(interesse);
        });
    }

    @ParameterizedTest(name = "[{index}] Suche mit mehreren Interessen: interesse1={0}, interesse1={1}")
    @CsvSource(LESEN + ',' + REISEN)
    @DisplayName("Suche mit mehreren Interessen")
    void getByInteressen(final String interesse1Str, final String interesse2Str) {
        // given
        final var suchkriterien = MultiValueMap.fromMultiValue(
            Map.of(INTERESSE_PARAM, List.of(interesse1Str, interesse2Str))
        );

        // when
        final var kunden = kundeRepo.get(suchkriterien);

        // then
        assertThat(kunden).isNotNull();
        final var embedded = kunden._embedded();
        softly.assertThat(embedded).isNotNull();
        final var kundenList = embedded.kunden();
        assertThat(kundenList)
            .isNotNull()
            .isNotEmpty();
        final var interesse1 = InteresseType.of(interesse1Str);
        final var interesse2 = InteresseType.of(interesse2Str);
        final var interessenList = List.of(interesse1, interesse2);
        kundenList.forEach(kunde -> {
            final var interessen = kunde.interessen();
            softly.assertThat(interessen)
                .isNotNull()
                .doesNotContainNull()
                .containsAll(interessenList);
        });
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Nested
    @DisplayName("Suche anhand der ID")
    class GetById {
        @ParameterizedTest(name = "[{index}] Suche mit vorhandener ID und JsonPath: id={0}")
        @ValueSource(strings = ID_VORHANDEN)
        @DisplayName("Suche mit vorhandener ID und JsonPath")
        @SuppressModernizer
        void getByIdJson(final String id) {
            // given

            // when
            final var response = kundeRepo.getByIdAsString(id);

            // then
            final var body = response.getBody();
            assertThat(body).isNotNull().isNotBlank();

            final var nachnamePath = "$.nachname";
            final String nachname = JsonPath.read(body, nachnamePath);
            softly.assertThat(nachname).matches(NACHNAME_PATTERN);

            final var emailPath = "$.email";
            final String email = JsonPath.read(body, emailPath);
            softly.assertThat(email).contains("@");

            final LinkDiscoverer linkDiscoverer = new HalLinkDiscoverer();
            final var selfLink = linkDiscoverer.findLinkWithRel("self", body).get().getHref();
            softly.assertThat(selfLink).isEqualTo(baseUrl + '/' + id);
        }

        @ParameterizedTest(name = "[{index}] Suche mit vorhandener ID: id={0}")
        @ValueSource(strings = ID_VORHANDEN)
        @DisplayName("Suche mit vorhandener ID")
        void getById(final String id) {
            // given

            // when
            final var response = kundeRepo.getById(id);

            // then
            final var kunde = response.getBody();
            assertThat(kunde).isNotNull();
            softly.assertThat(kunde.nachname()).isNotNull();
            softly.assertThat(kunde.email()).isNotNull();
            softly.assertThat(kunde.adresse().plz()).isNotNull();
            softly.assertThat(kunde._links().self().href()).endsWith("/" + id);
        }

        @ParameterizedTest(name = "[{index}] Suche mit syntaktisch ungueltiger oder nicht-vorhandener ID: {0}")
        @ValueSource(strings = ID_NICHT_VORHANDEN)
        @DisplayName("Suche mit syntaktisch ungueltiger oder nicht-vorhandener ID")
        void getByIdNichtVorhanden(final String id) {
            // when
            final var exc = catchThrowableOfType(
                HttpClientErrorException.NotFound.class,
                () -> kundeRepo.getById(id)
            );

            // then
            assertThat(exc.getStatusCode()).isEqualTo(NOT_FOUND);
        }
    }
}
