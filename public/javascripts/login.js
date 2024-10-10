function getCookie(cookieName) {
  let name = cookieName + "=";
  let decodedCookie = decodeURIComponent(document.cookie);
  let ca = decodedCookie.split(';');
  for(let i = 0; i <ca.length; i++) {
    let c = ca[i];
    while (c.charAt(0) === ' ') {
      c = c.substring(1);
    }
    if (c.indexOf(name) === 0) {
      return c.substring(name.length, c.length);
    }
  }
  return "";
}

document.addEventListener("DOMContentLoaded", function() {
  const username = getCookie('username');
  const email = getCookie('email');

  if (username && email) {
    document.getElementById('username').value = username;
    document.getElementById('email').value = email;
  }
});

document.getElementById("login-form").onsubmit = function(event) {
  document.getElementById("username-error").innerText = "";
  document.getElementById("email-error").innerText = "";

  const username = document.getElementById("username").value.trim();
  const email = document.getElementById("email").value.trim();
  let isValid = true;

  if (username === "") {
    document.getElementById("username-error").innerText = "Username is required.";
    isValid = false;
  }

  if (email === "") {
    document.getElementById("email-error").innerText = "Email is required.";
    isValid = false;
  }

  if (!isValid) {
    event.preventDefault();
  }
};
