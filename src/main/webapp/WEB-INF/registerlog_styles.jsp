<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<style>
  /* styles.css */
  body {
    font-family: Arial, sans-serif;
    margin: 20px auto;
    max-width: 600px;
    padding: 10px;
    background-color: #f9f9f9;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  }

  h2 {
    text-align: center;
    color: #333;
  }

  form {
    display: flex;
    flex-direction: column;
  }

  label {
    margin-top: 10px;
    font-weight: bold;
  }

  input, button {
    padding: 8px;
    margin-top: 5px;
    font-size: 14px;
  }

  input:invalid {
    border: 1px solid red;
  }

  input:valid {
    border: 1px solid green;
  }

  button {
    background-color: #4CAF50;
    color: white;
    border: none;
    margin-top: 15px;
    cursor: pointer;
  }

  button:hover {
    background-color: #45a049;
  }

  .error {
    color: red;
    margin-top: 10px;
  }

</style>