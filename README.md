# 서비스명
서비스 설명

---

## 프로젝트 설명
> 추후 내용 추가
---

## Git 커밋명 규칙
```
- **Feat : 신규 기능 추가**
- **Fix : 버그 수정 **
- Build : 빌드 관련 파일 수정
- Chore : 기타 수정
- Ci : 지속적 개발에 따른 수정
- Docs : 문서 수정
- Style : 코드 스타일, 포멧형식 관련
- Refactor : 리팩토링
- Test : 테스트 코드 수정
```

---

## 디렉토리 구조

```
tree명령어와 gpt 이용하면 쓸만하게 나옴
```

---

## 개발 실행/점검 커멘드


---

## 각 branch 용도
<img width="523" height="461" alt="Image" src="https://github.com/user-attachments/assets/c7d35c8d-8763-4bef-8753-2a3e498ef278" />

> ### main(*중요)
>> 메인 branch  
>> main에 대한 모든 업데이트는 **release에서 먼저 테스트**하여 이상이 없을 시에 **팀원 모두의 동의**를 구하고한다.
>
> ### develop
>> 개발버전 브랜치.
>> feature에서 병합받는 브랜치입니다. 해당 브랜치에서는 버그 픽스만을 권장, 기능추가같은건 feature에서 작업 후 병합해야합니다.
>
> ### feature/{기능명 or 임의명}
>> 각 기능별, 서비스별 개발중인 브랜치.  
>> {} 대괄호 안에는 해당 기능의 이름 or 개인용 브랜치면 원하는 이름으로 생성한다.  
>>  
>> ex) feature/이름영어로, feature/login  
>> 개인용 브랜치는 자기 이름의 이니셜을 추천합니다.
>
> ### release
>> main으로 올리기전 준비하는 브랜치  
>> develop에서 병합받는 브랜치입니다.  
>> release에서의 작업은 오직 버그 픽스만을 권장합니다.
> ### hotfixes
>> main에서 나온 버그를 빠르게 수정하는 브랜치


---


## Git 초기설정.(추후 레포 생성 후 수정하기)
>터미널/cmd/git bash에서 프로젝트를 저장할 위치로 이동 후 아래 코드 입력
>> git clone 아직 레포가 없음
>
> vscode(다른 ide도 가능)에서 해당 프로젝트를 폴더 열기  
> vscode의 터미널에서 아래 코드 입력
>> git switch develop  
>> git switch -c [원하는 개인 branch명]  
>
> 좌측 소스 제어(깃)에서 [게시 Branch] 버튼 클릭
> 
> 아래 코드를 입력하여 각 branch가 제대로 추가되었는지 확인  
>> git branch -r

---

## GIT 명령어 모음
> branch 새로 생성 후 해당 branch로 즉시 전환
>> git switch -c [branch명]
>
> 로컬(개인컴) branch 목록 조회(*로 강조된 곳이 현재 branch)
>> git branch
>
> 원격(깃허브) branch 목록 조회(*로 강조된 곳이 현재 branch)
>> git branch -r
>
> branch 전환 코드 2가지(최근에는 switch를 더 자주 사용한다고함. checkout은 참고용)
>> git switch [branch명]
>> git checkout [branch명]
>
> branch 병합하기(현재 branch를 대상으로 명령어의 branch를 덮어씌우는 느낌)
>> git merge [branch명]
>

---

## .gitignore 사용법 예시 (원하는 파일의 커밋제한)
> 초기 레포지토리 생성 시 사용 언어 넣어서 초기 세팅하는게 편리함

>// 1. '파일명'으로 제외하는 방법 (* 해당 방법은 경로 상관없이 지정한 파일명으로 모두 제외할 수 있다)  
>ignoreFileName.js
>
>// 2. 특정 '파일'만 제외하는 방법 (* 현재 기준을 .ignore파일이 있는 경로라고 생각하면 된다)  
>src/ignoreFileName.js
>
>// 3. 특정 '디렉토리' 기준 이하 파일들 제외 방법  
>node_module/
>
>// 4. 특정 디렉토리 하위의 특정 '확장자' 제외하는 방법  
>src/*.txt
>
>// 5. 특정 디렉토리 하위의 그 하위의 특정 '확장자' 제외하는 방법  
>src/**/*.txt
>
>// 6. 특정 '확장자' 제외하기  
>.txt
>
>// 7. 4번 특정 '확장자'에서 일부 제외 할 파일  
>!manual.txt

---

