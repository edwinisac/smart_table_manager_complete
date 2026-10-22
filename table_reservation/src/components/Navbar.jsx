import { NavLink, useNavigate } from "react-router-dom";
import "./navbar.css";



function Navbar() {
  const navigate=useNavigate();
  return (
    <nav className="navbar">
      <div className="navbar-container">

        {/* Logo */}
        <div className="navbar-logo" onClick={()=>{navigate("/")}}>
          <div className="logo-icon">🍽️</div>
          <span>TableBook</span>
        </div>

        {/* Navigation */}
        <div className="navbar-links">
          <NavLink to="/">Select Table</NavLink>
          <NavLink to="/bookings">Previous Bookings</NavLink>
          <NavLink to="/profile">Profile</NavLink>
          <button onClick={()=>navigate("/login")} className="logout-btn">Logout</button>
        </div>

      </div>
    </nav>
  );
}

export default Navbar;