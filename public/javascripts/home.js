function autoGrow(element) {
  element.style.height = "auto";
  element.style.height = (element.scrollHeight) + "px";
}

function autoGrow2(element) {
  element.style.height = "auto";
  element.style.height = (element.scrollHeight) + "px";
}

function typeMessage(message, callback) {
  const chatBox = document.getElementById('chat-box');
  const messageElem = document.createElement('div');
  messageElem.className = 'message bot';
  chatBox.appendChild(messageElem);

  let index = 0;
  function typeNextLetter() {
    if (index < message.length) {
      messageElem.textContent += message.charAt(index);
      index++;
      chatBox.scrollTop = chatBox.scrollHeight;
      setTimeout(typeNextLetter, 50);
    } else if (callback) {
      callback();
    }
  }
  typeNextLetter();
}

function appendMessage(sender, message) {
  const chatBox = document.getElementById('chat-box');
  const messageElem = document.createElement('div');
  messageElem.className = sender === 'user' ? 'message user' : 'message bot';
  messageElem.textContent = message;
  chatBox.appendChild(messageElem);

  chatBox.scrollTop = chatBox.scrollHeight;
}

function sendMessage() {
  const userInput = document.getElementById('user-input').value;
  const userInstruction = document.getElementById('user-instruction').value;

  if (!userInput) return;
  appendMessage('user', userInput);

  const data = {
    instruction: userInstruction,
    question: userInput,
  };

  axios.post('/submitQuery', data)
    .then(response => {
      typeMessage(response.data.response);
    })
    .catch(error => {
      console.log(error);
      appendMessage('bot', 'Error: Could not fetch response');
    });

  document.getElementById('user-input').value = '';
  document.getElementById('user-instruction').value = '';
  autoGrow(document.getElementById('user-input'));
  autoGrow(document.getElementById('user-instruction'));
}

function loadPreviousQueries() {
  axios.get('/allQueries')
    .then(response => {
      const queries = response.data;
      queries.forEach(query => {
        appendMessage('user', query.question);
        appendMessage('bot', query.response);
      });
    })
    .catch(error => {
      console.log(error);
      appendMessage('bot', 'Error fetching previous chat history.');
    });
}

function logout() {
  axios.get('/logout')
    .then(response => {
      window.location.href = '/';
    })
    .catch(error => {
      console.error('Error logging out:', error);
    });
}

document.addEventListener("DOMContentLoaded", function() {
  loadPreviousQueries();
  autoGrow(document.getElementById('user-instruction'));
  autoGrow2(document.getElementById('user-input'));
});
