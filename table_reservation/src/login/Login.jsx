import { useState } from "react";
import "./login.css";
import { useNavigate } from "react-router-dom";

function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const navigate=useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();
    if(username==="user" && password==="123"){
        navigate("/")
    }
    else{
        alert("login failed");
    }
    

    // login mock handling  
  };

  return (
    <div className="login-page">
      <div className="login-card">

        <h2>Welcome Back</h2>
        <p className="login-subtitle">Login to your account</p>

        <form onSubmit={handleSubmit}>

          <div className="form-group">
            <label htmlFor="username">Username</label>
            <input
              type="text"
              id="username"
              placeholder="Enter your username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="password">Password</label>
            <input
              type="password"
              id="password"
              placeholder="Enter your password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>

          <button type="submit" className="login-button">
            Login
          </button>

        </form>

        <p className="register-text">
          Don't have an account? <a href="/register">Register</a>
        </p>

      </div>
    </div>
  );
}

export default Login;