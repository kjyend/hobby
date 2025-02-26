## 취미 기록 Web Page V4

취미 기록 Web Page V4
이 프로젝트는 개인의 취미를 기록하고 공유할 수 있는 웹사이트입니다. 
V4에서는 **좋아요 및 조회수 기능 추가, 게시글 목록 개선**을 통해 사용자 경험을 향상시켰습니다.
사용자는 다양한 취미를 자유롭게 등록하고, 활동 내용을 정리하여 언제든지 돌아볼 수 있습니다. 
취미를 기록하는 즐거움을 통해 더 많은 사람들과 소통하고, 새로운 취미를 발견하는 기회를 가질 수 있습니다. 
나만의 취미 세상을 만들어보세요!

| **버전** | **링크**       |
|----------|----------------|
| 취미 기록 게시판 V1       | [취미 Web Page V1](https://github.com/kjyend/hobbyproject) |
| 취미 기록 게시판 V2       | [취미 Web Page V2](https://github.com/kjyend/hobbyprojectV2) |
| 취미 기록 게시판 V3       | [취미 Web Page V3](https://github.com/kjyend/hobbyprojectV3) |
| 취미 기록 게시판 V4       | [취미 Web Page V4](https://github.com/kjyend/hobbyprojectV4) |
| 취미 기록 게시판 V5       | [취미 Web Page V5](https://github.com/kjyend/hobbyprojectV5) |

## 개요

개발 기간 : 2024.1.4 ~ 2024.1.24

멤버 : 김준영(FE/BE)

## 시작 가이드

Installation
```
$ git clone https://github.com/kjyend/hobbyprojectV4.git
$ cd hobbyprojectV4
```
Backend
```
$ ./gradlew build
$ ./gradlew bootRun
```

## 기술 스택

Language (BE) : Java

Language (FE) : JavaScript

Library & Framework : Spring Boot, Spring, Thymeleaf, Hibernate 

Database : MySQL

ORM : JPA 

Environment : IntelliJ, Git, GitHub 

Monitoring : Prometheus, Grafana

Performance Testing : JMeter

## V4 주요 변경 사항
✅ 좋아요 기능 추가
* 사용자는 게시글에 **좋아요**를 누를 수 있음
* 좋아요가 상세 페이지에 표시됨

✅ 게시글 목록 UI 개선
* **제목**: 최대 10글자까지만 표시
* **글쓴이**: 내가 작성한 글이면 내 이름이 보이도록 변경
* **작성일시, 조회수, 좋아요** 정보 추가

## 화면 구성

게시글 리스트 당일에 올린글과 다른 날 올린글
![화면 캡처 2025-02-25 141958](https://github.com/user-attachments/assets/a8a13605-de21-4907-9ac8-4acd042186e2)


작성된 게시글 좋아요. 안 누른 화면
![스크린샷 2025-02-25 142046](https://github.com/user-attachments/assets/f9c2ab66-5c3e-414c-bff0-68bba190f463)


작성된 게시글 좋아요. 누른 화면
![스크린샷 2025-02-25 144000](https://github.com/user-attachments/assets/79b1683b-d6aa-43db-ba26-949d37df9dd0)


## ERD
![스크린샷 2025-02-25 145719](https://github.com/user-attachments/assets/4b3f5dc2-f5bd-4e17-b453-d44edf291396)
