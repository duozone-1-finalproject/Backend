package com.example.finalproject.dart_viewer.repository;

import com.example.finalproject.dart_viewer.entity.UserVersion;
import lombok.RequiredArgsConstructor;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.FieldValue;
import org.opensearch.client.opensearch._types.Refresh;
import org.opensearch.client.opensearch._types.SortOrder;
import org.opensearch.client.opensearch._types.query_dsl.Query;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.example.finalproject.dart_viewer.constant.VersionConstant.INDEX_NAME;

@Repository
@RequiredArgsConstructor
public class UserVersionRepository {

    private final OpenSearchClient client;

    /**
     * userId로 전체 버전 검색
     */
    public List<UserVersion> findByUserId(Long userId) throws IOException {
        Query query = Query.of(q -> q
                .term(t -> t
                        .field("user_id") // entity의 JsonProperty 기준
                        .value(FieldValue.of(userId))
                )
        );

        var response = client.search(s -> s
                        .index(INDEX_NAME)
                        .query(query)
                        .size(100),
                UserVersion.class
        );

        return response.hits().hits().stream()
                .map(hit -> hit.source())
                .toList();
    }

    /**
     * userId + version 으로 단건 조회
     */
    public Optional<UserVersion> findByUserIdAndVersion(Long userId, String version) throws IOException {
        Query query = Query.of(q -> q
                .bool(b -> b
                        .must(List.of(
                                Query.of(m -> m.term(t -> t.field("user_id").value(FieldValue.of(userId)))),
                                Query.of(m -> m.term(t -> t.field("version").value(FieldValue.of(version))))
                        ))
                )
        );

        var response = client.search(s -> s
                        .index(INDEX_NAME)
                        .query(query)
                        .size(1),
                UserVersion.class
        );

        return response.hits().hits().isEmpty()
                ? Optional.empty()
                : Optional.ofNullable(response.hits().hits().get(0).source());
    }

    /**
     * userId가 같고 version이 다른 것 중 최신(id DESC) 하나
     */
    public Optional<UserVersion> findTopByUserIdAndVersionNotOrderByIdDesc(Long userId, String version) throws IOException {
        Query query = Query.of(q -> q
                .bool(b -> b
                        .must(m -> m.term(t -> t.field("user_id").value(FieldValue.of(userId))))
                        .mustNot(m -> m.term(t -> t.field("version").value(FieldValue.of(version))))
                )
        );

        var response = client.search(s -> s
                        .index(INDEX_NAME)
                        .query(query)
                        .size(1)
                        .sort(sort -> sort
                                .field(f -> f
                                        .field("version_number") // 정렬 기준
                                        .order(SortOrder.Desc))),
                UserVersion.class
        );

        return response.hits().hits().isEmpty()
                ? Optional.empty()
                : Optional.ofNullable(response.hits().hits().get(0).source());
    }

    /**
     * 저장 (id를 문서 ID로 사용)
     *
     * @return
     */
    public UserVersion save(UserVersion userVersion) throws IOException {
        String docId = userVersion.getUserId() + "_" + userVersion.getVersion();
        client.index(i -> i
                .index(INDEX_NAME)
                .id(docId)
                .document(userVersion)
                .refresh(Refresh.WaitFor)
        );
        return userVersion;
    }


    public void delete(Long userId) throws IOException {
        var searchResponse = client.search(s -> s
                        .index(INDEX_NAME)
                        .query(q -> q
                                .bool(b -> b
                                        .must(Query.of(q1 -> q1.term(t -> t.field("user_id").value(FieldValue.of(userId)))))
                                        .must(Query.of(q2 -> q2.term(t -> t.field("version").value(FieldValue.of("editing")))))
                                )
                        )
                        .size(1),  // 하나만 삭제
                UserVersion.class
        );

        if (!searchResponse.hits().hits().isEmpty()) {
            String docId = searchResponse.hits().hits().get(0).id();
            client.delete(d -> d
                    .index(INDEX_NAME)
                    .id(docId)
                    .refresh(Refresh.WaitFor)
            );
        } else {
            throw new RuntimeException("편집중인 버전이 없습니다.");
        }
    }
}
