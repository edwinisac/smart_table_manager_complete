import "./profile.css";

function Profile() {
  return (
    <div className="profile-page">
      <div className="profile-card">

        {/* Profile Header */}
        <div className="profile-header">
          <div className="profile-avatar">
            E
          </div>

          <div>
            <h2>Edwin Isac</h2>
            <p>TableBook Member</p>
          </div>
        </div>

        {/* Personal Information */}
        <div className="profile-section">
          <h3>Personal Information</h3>

          <div className="profile-details">
            <div className="detail-item">
              <span>Name</span>
              <strong>Edwin Isac</strong>
            </div>

            <div className="detail-item">
              <span>Username</span>
              <strong>edwin</strong>
            </div>

            <div className="detail-item">
              <span>Email</span>
              <strong>edwin@example.com</strong>
            </div>

            <div className="detail-item">
              <span>Phone</span>
              <strong>9876543210</strong>
            </div>
          </div>
        </div>

        {/* Account Information */}
        <div className="profile-section">
          <h3>Account Information</h3>

          <div className="profile-details">
            <div className="detail-item">
              <span>Total Bookings</span>
              <strong>12</strong>
            </div>

            <div className="detail-item">
              <span>Active Bookings</span>
              <strong>2</strong>
            </div>
          </div>
        </div>

        {/* Actions */}
        <div className="profile-actions">
          <button className="edit-btn">
            Edit Profile
          </button>

          <button className="password-btn">
            Change Password
          </button>
        </div>

      </div>
    </div>
  );
}

export default Profile;