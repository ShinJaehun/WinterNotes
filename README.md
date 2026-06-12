# WinterNotes

WinterNotes는 Android 노트앱 구조를 학습하기 위해 만든 첫 번째 겨울 노트 프로젝트입니다. 기본적인 노트 작성, 조회, 수정, 삭제 흐름을 Room DB로 구현하고, 앱 화면 위에 눈송이가 내려오는 시각 효과를 더했습니다. 흔한 노트앱 구조를 바탕으로 하되, 겨울 분위기의 UI 효과를 직접 구현해 Android View, Navigation, Room, Coroutine 사용을 연습한 프로젝트입니다.

---

## 이 저장소에 대하여

이 저장소는 Winter Notes 계열의 초기 버전입니다.

Winter Notes 계열은 같은 노트앱 아이디어를 바탕으로 여러 저장 구조와 Android 앱 구성을 실험한 프로젝트군입니다.

- `WinterNotes`: Room 기반 초기 버전
- `WinterNotesV2`: Room + Firebase 혼합 실험 버전
- `WinterNotesRoom`: Room 중심으로 다시 정리한 로컬 저장 버전
- `WinterNotesFirebase`: Firebase 중심 클라우드 저장 실험 버전

이 저장소는 그중 가장 먼저 만든 버전으로, Android 노트앱의 기본 CRUD 구조와 눈 내리는 UI 효과를 구현하는 데 초점을 두었습니다.

---

## 주요 특징

### 1. Room 기반 노트 저장

노트 데이터는 Room DB를 사용해 로컬에 저장합니다.

노트에는 다음과 같은 정보가 포함됩니다.

- 제목
- 날짜
- 부제목
- 본문
- 이미지 경로
- 색상
- 웹 링크

Room DAO를 통해 다음 기능을 제공합니다.

- 전체 노트 조회
- 노트 ID 기반 조회
- 제목, 부제목, 본문 검색
- 노트 삭제
- 노트 추가 및 수정

### 2. 눈 내리는 UI 효과

WinterNotes의 가장 큰 특징은 화면 위에 눈송이가 내려오는 효과입니다.

앱 실행 시 여러 개의 눈송이 객체를 만들고, Coroutine을 사용해 주기적으로 위치를 갱신합니다. 각 눈송이는 랜덤한 위치, 속도, 흔들림을 가지고 움직이며 화면 아래로 내려가면 다시 위쪽에서 나타납니다.

이 기능은 단순한 노트앱에 겨울 분위기를 더하기 위해 직접 구현한 시각 효과입니다.

### 3. Android Navigation 사용

앱 화면 전환은 Android Navigation을 사용합니다.

노트 목록, 노트 상세, 노트 작성/수정 화면을 분리하고, Navigation Controller를 통해 화면을 이동하는 구조를 연습했습니다.

### 4. ViewBinding 사용

XML View 기반 UI에서 ViewBinding을 사용했습니다.

`findViewById`를 직접 반복해서 쓰는 방식보다 안전하게 View에 접근할 수 있도록 구성했습니다.

### 5. 이미지가 포함된 노트 구조

노트에 이미지 경로를 저장할 수 있도록 설계했습니다.

외부 저장소 권한과 이미지 선택 흐름을 실험하면서, 텍스트 중심 노트앱에서 이미지 노트앱으로 확장할 수 있는 구조를 만들었습니다.

---

## 기술 스택

- Android
- Kotlin
- Room
- Coroutine
- Android Navigation
- ViewBinding
- AppCompat
- Material Components
- ConstraintLayout

---

## 프로젝트 성격

WinterNotes는 완성형 상용 노트앱이라기보다, Android 앱 구조를 익히기 위한 학습 프로젝트입니다.

특히 다음 내용을 연습하는 데 초점을 두었습니다.

- Android 프로젝트 기본 구조
- Room Entity / DAO 구성
- 로컬 DB 기반 CRUD
- Navigation 기반 화면 이동
- ViewBinding 사용
- Coroutine을 이용한 반복 UI 갱신
- 이미지가 포함된 노트 데이터 구조
- 직접 구현한 간단한 애니메이션 효과

---

## Winter Notes 계열에서의 위치

이 저장소는 Winter Notes 계열의 출발점입니다.

이후 버전에서는 같은 노트앱 아이디어를 바탕으로 다음과 같은 실험을 이어갔습니다.

- Gradle Kotlin DSL과 version catalog 도입
- Firebase Auth, Firestore, Storage 실험
- Room 중심 구조와 Firebase 중심 구조 분리
- 도메인 모델과 저장 모델 분리
- 이미지 처리 방식 개선
- Android 아키텍처 학습

---

## 현재 상태

- Room 기반 노트 저장 구현
- 노트 목록/조회/검색/삭제/저장 흐름 구현
- 눈 내리는 UI 효과 구현
- Android Navigation 적용
- ViewBinding 적용
- 학습용 초기 버전으로 보관

---

## 앞으로의 개선 방향

이 저장소 자체를 계속 확장하기보다는, 이후 버전인 `WinterNotesRoom`과 `WinterNotesFirebase`에서 구조를 나누어 실험했습니다.

가능한 개선 방향은 다음과 같습니다.

- SnowEffect 로직 분리
- Activity에 집중된 상태 관리 개선
- Repository 계층 도입
- UI 상태와 데이터 상태 분리
- 이미지 저장 방식 개선
- 테스트 코드 추가

---

## 저작권

- WinterNotes는 신재훈이 만들었습니다.
- WinterNotes는 GNU GPL을 따릅니다.
