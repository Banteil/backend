/**
 * auth-utils.js (확장형)
 */
const authUtils = {
  /**
   * @param {Object} options
   * - url: API 주소 (필수)
   * - idValue: 검증할 대상의 PK 값 (bno, rno 등)
   * - idKey: 서버에서 요구하는 PK 키 이름 (기본값: 'id')
   * - successCallback: 검증 성공 시 실행할 함수
   */
  verifyPassword: function ({ url, idValue, idKey = "id", successCallback }) {
    Swal.fire({
      title: "비밀번호 확인",
      text: "본인 확인을 위해 비밀번호를 입력해주세요.",
      input: "password",
      showCancelButton: true,
      confirmButtonText: "확인",
      showLoaderOnConfirm: true,
      preConfirm: (inputPassword) => {
        // 동적으로 바디 구성
        const body = { password: inputPassword };
        body[idKey] = idValue;

        return fetch(url, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            [header]: token,
          },
          body: JSON.stringify(body),
        })
          .then((response) => {
            if (!response.ok) throw new Error("서버 통신 오류");
            return response.json();
          })
          .then((isMatch) => {
            if (!isMatch) throw new Error("비밀번호가 일치하지 않습니다.");
            return inputPassword;
          })
          .catch((error) => {
            Swal.showValidationMessage(`오류: ${error.message}`);
          });
      },
      allowOutsideClick: () => !Swal.isLoading(),
    }).then((result) => {
      if (result.isConfirmed) {
        successCallback(result.value);
      }
    });
  },
};
