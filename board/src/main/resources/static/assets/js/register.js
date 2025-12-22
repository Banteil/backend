const token = document.querySelector('meta[name="_csrf"]').content;
const header = document.querySelector('meta[name="_csrf_header"]').content;

document.addEventListener("DOMContentLoaded", function () {
  const registerForm = document.getElementById("registerForm");

  if (registerForm) {
    registerForm.addEventListener("submit", function (e) {
      e.preventDefault();

      const memberData = {
        email: document.getElementById("email").value,
        password: document.getElementById("password").value,
        name: document.getElementById("name").value,
      };

      // 1단계: 회원 가입 API 호출
      fetch("/member/register", {
        method: "POST",
        headers: { "Content-Type": "application/json", [header]: token },
        body: JSON.stringify(memberData),
      })
        .then((response) => {
          if (!response.ok)
            return response.text().then((msg) => {
              throw new Error(msg);
            });
          return response.text();
        })
        .then((email) => {
          // 2단계: 가입 성공 시 시큐리티 로그인 절차 진행
          Swal.fire({
            title: "가입 완료!",
            text: "로그인을 진행합니다...",
            icon: "success",
            showConfirmButton: false,
            timer: 1000,
          }).then(() => {
            performLogin(memberData.email, memberData.password);
          });
        })
        .catch((error) => {
          Swal.fire("오류 발생", error.message, "error");
        });
    });

    // 시큐리티 로그인을 수행하는 별도 함수
    function performLogin(username, password) {
      const params = new URLSearchParams();
      params.append("username", username);
      params.append("password", password);

      // 시큐리티 설정에 등록된 loginProcessingUrl (기본값 /login) 호출
      fetch("/member/login", {
        method: "POST",
        headers: { [header]: token },
        body: params,
      }).then((response) => {
        if (response.redirected) {
          window.location.href = response.url; // 핸들러가 지정한 경로로 이동
        } else {
          window.location.href = "/"; // 기본 홈으로 이동
        }
      });
    }
  }
});
