package com.example.finalproject.dart.repository;

import com.example.finalproject.dart.entity.CompanyOverview;
import com.example.finalproject.dart.entity.CorpCode;
import com.example.finalproject.login_auth.entity.User;
import lombok.RequiredArgsConstructor;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.FieldValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Repository
public interface CorpCodeRepository extends JpaRepository<CorpCode, Long>{
    List<CorpCode> findTop100ByCorpNameContainingOrderByCorpNameAsc(String corpName);
}
