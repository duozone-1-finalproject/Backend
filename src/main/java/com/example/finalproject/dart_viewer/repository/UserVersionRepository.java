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
     * userId 로 모든 회사의 모든 버전 검색
     */
    public List<UserVersion> findByUserId(Long userId) throws IOException {
        Query query = Query.of(q -> q
                .term(t -> t.field("user_id").value(FieldValue.of(userId)))
        );

        var response = client.search(s -> s
                        .index(INDEX_NAME)
                        .query(query)
                        .size(1000), // 충분히 큰 값으로 설정
                UserVersion.class
        );

        return response.hits().hits().stream()
                .map(hit -> hit.source())
                .toList();
    }

    /**
     * userId + corpCode 로 전체 버전 검색
     */
    public List<UserVersion> findByUserIdAndCorpCode(Long userId, String corpCode) throws IOException {
        Query query = Query.of(q -> q
                .bool(b -> b
                        .must(List.of(
                                Query.of(m -> m.term(t -> t.field("user_id").value(FieldValue.of(userId)))),
                                Query.of(m -> m.term(t -> t.field("corp_code").value(FieldValue.of(corpCode))))
                        ))
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
     * userId + corpCode + version 으로 단건 조회
     */
    public Optional<UserVersion> findByUserIdAndCorpCodeAndVersion(Long userId, String corpCode, String version) throws IOException {
        Query query = Query.of(q -> q
                .bool(b -> b
                        .must(List.of(
                                Query.of(m -> m.term(t -> t.field("user_id").value(FieldValue.of(userId)))),
                                Query.of(m -> m.term(t -> t.field("corp_code").value(FieldValue.of(corpCode)))),
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
     * userId + corpCode가 같고 version이 다른 것 중 최신(id DESC) 하나
     */
    public Optional<UserVersion> findTopByUserIdAndCorpCodeAndVersionNotOrderByIdDesc(Long userId, String corpCode, String version) throws IOException {
        Query query = Query.of(q -> q
                .bool(b -> b
                        .must(List.of(
                                Query.of(m -> m.term(t -> t.field("user_id").value(FieldValue.of(userId)))),
                                Query.of(m -> m.term(t -> t.field("corp_code").value(FieldValue.of(corpCode))))
                        ))
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
        String docId = userVersion.getUserId() + "_" + userVersion.getCorpCode() + "_" + userVersion.getVersion();
        client.index(i -> i
                .index(INDEX_NAME)
                .id(docId)
                .document(userVersion)
                .refresh(Refresh.WaitFor)
        );
        return userVersion;
    }


    /**
     * 특정 버전 삭제
     */
    public void deleteVersion(Long userId, String corpCode, String version) throws IOException {
        String docId = userId + "_" + corpCode + "_" + version;
        client.delete(d -> d
                .index(INDEX_NAME)
                .id(docId)
                .refresh(Refresh.WaitFor)
        );
    }

    /**
     * 특정 회사의 모든 버전 삭제
     */
    public void deleteCompany(Long userId, String corpCode) throws IOException {
        Query query = Query.of(q -> q
                .bool(b -> b
                        .must(List.of(
                                Query.of(m -> m.term(t -> t.field("user_id").value(FieldValue.of(userId)))),
                                Query.of(m -> m.term(t -> t.field("corp_code").value(FieldValue.of(corpCode))))
                        ))
                )
        );

        var response = client.search(s -> s
                        .index(INDEX_NAME)
                        .query(query)
                        .size(1000),
                UserVersion.class
        );

        // 모든 문서 삭제
        for (var hit : response.hits().hits()) {
            client.delete(d -> d
                    .index(INDEX_NAME)
                    .id(hit.id())
                    .refresh(Refresh.WaitFor)
            );
        }
    }
}
