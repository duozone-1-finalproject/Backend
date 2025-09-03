// 단일회사 주요계정
package com.example.finalproject.apitest.repository.periodic;

import com.example.finalproject.apitest.entity.periodic.DartSingleCompanyKeyAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DartSingleCompanyKeyAccountRepository extends JpaRepository<DartSingleCompanyKeyAccount, Long> {
    /*
     *
     * 고유번호(corpCode)로 단일회사 주요계정 목록을 조회합니다.
     * @param corpCode 회사의 고유번호
     * @return 조회된 DartSingleCompanyKeyAccount 엔티티 리스트
     */
    List<DartSingleCompanyKeyAccount> findByRceptNo(String rceptNo);
}

