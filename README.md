# STJ-Company
SoftwareEngineering

<br/>

Unity3D NDK 다운로드 링크
https://drive.google.com/file/d/1QbpAQutWsJRVJtYk8EhErX0s9t0xEsjl/view?usp=drive_link

< 주의 >

★ 해당 App 에 Unity Game 도 포함 되어있어 아래 경로에 해당 폴더가 존재해야 합니다.
C:\Program Files\Unity\Hub\Editor\2021.3.29f1\Editor\Data\PlaybackEngines\AndroidPlayer\NDK

    ◆ 첨부된 NDK.zip 파일을 압축 해제 하여 위 경로에 넣어야 합니다.
    ◆ 해당 경로가 존재 하지 않을 경우 CMD ( 명령 프롬프트 ) 창을 활성화 하여 아래 명령어 입력 후 붙여넣기
    mkdir C:\Program Files\Unity\Hub\Editor\2021.3.29f1\Editor\Data\PlaybackEngines\AndroidPlayer\NDK
    ◆ 애뮬레이터 API 34 구글 플레이스토어 버전 설치
    ◆ SDK 34 설치

★ 빌드 중 IL2CPP 오류가 나는 경우
단축키 ( Ctrl+Alt+Shift+S )로 Project Structure 이동 후 SDK Location에서 Download Android NDK.
상단 메뉴바 Build에서 Rebuild Project 실행

    if 여전히 오류가 발생한다면
      1. 애뮬레이터 Wipe Data로 캐시 삭제
      2. 안드로이드 스튜디오 재시작
      3. 재시도

    * 원활한 테스트를 위해서 기본적으로 Feed 와 일정이 등록되어있는
    ( wogns@naver.com	/ 123456 ) 또는
    ( admin@naver.com 	/ 123456 ) 또는
    ( test@naver.com   	/ 123456 ) 으로 로그인 하시는것을 권장 드립니다.
