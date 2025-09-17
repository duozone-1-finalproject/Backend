package com.example.finalproject.dart.repository;

import com.example.finalproject.dart.entity.CompanyOverview;
import lombok.RequiredArgsConstructor;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.FieldValue;
import org.opensearch.client.opensearch._types.query_dsl.QueryBuilders;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CompanyOverviewRepository {

    private final OpenSearchClient client;

    private static final String INDEX_NAME = "company_overview";

    public Optional<CompanyOverview> findByCorpCode(String corpCode) throws IOException {
        var response = client.get(g -> g
                        .index(INDEX_NAME)
                        .id(corpCode),
                CompanyOverview.class
        );
        return Optional.ofNullable(response.source());
    }

    public Optional<CompanyOverview> findByCorpName(String corpName) throws IOException {
        var response = client.search(s -> s
                        .index(INDEX_NAME)
                        .query(q -> q
                                .term(t -> t
                                        .field("corp_name")
                                        .value(FieldValue.of(corpName))
                                )
                        ),
                CompanyOverview.class
        );

        if (response.hits().hits().isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(response.hits().hits().get(0).source());
    }

    public List<CompanyOverview> findAll() throws IOException {
        var response = client.search(s -> s
                        .index(INDEX_NAME)
                        .query(q -> q.matchAll(m -> m)),
                CompanyOverview.class
        );

        return response.hits().hits().stream()
                .map(hit -> hit.source())
                .toList();
    }

    public List<CompanyOverview> findByCorpNameContaining(String word) throws IOException {
        var response = client.search(s -> s
                        .index(INDEX_NAME)
                        .query(q -> q
                                .match(m -> m
                                        .field("corp_name")
                                        .query(FieldValue.of(word))
                                )
                        ),
                CompanyOverview.class
        );

        return response.hits().hits().stream()
                .map(hit -> hit.source())
                .toList();
    }

    public void save(CompanyOverview company) throws IOException {
        client.index(i -> i
                .index(INDEX_NAME)
                .id(company.getCorpCode())
                .document(company)
        );
    }

    public void saveAll(List<CompanyOverview> companies) throws IOException {
        for (CompanyOverview company : companies) {
            save(company);
        }
    }
}
