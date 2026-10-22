import "./bookings.css";

const activeBookings = [
  {
    id: 1,
    table: "T12",
    date: "20 Aug 2026",
    time: "7:30 PM",
    guests: 4,
    status: "Confirmed",
  },
  {
    id: 2,
    table: "T05",
    date: "22 Aug 2026",
    time: "8:00 PM",
    guests: 2,
    status: "Confirmed",
  },
];

const previousBookings = [
  {
    id: 3,
    table: "T08",
    date: "15 Aug 2026",
    time: "7:00 PM",
    guests: 3,
    status: "Completed",
  },
  {
    id: 4,
    table: "T03",
    date: "10 Aug 2026",
    time: "8:30 PM",
    guests: 5,
    status: "Cancelled",
  },
];

function BookingCard({ booking, active }) {
  return (
    <div className="booking-card">
      <div className="booking-card-header">
        <div>
          <span className="booking-label">Table</span>
          <h3>{booking.table}</h3>
        </div>

        <span
          className={`booking-status ${
            booking.status === "Confirmed"
              ? "confirmed"
              : booking.status === "Completed"
              ? "completed"
              : "cancelled"
          }`}
        >
          {booking.status}
        </span>
      </div>

      <div className="booking-details">
        <div className="booking-detail">
          <span>📅</span>
          <div>
            <small>Date</small>
            <p>{booking.date}</p>
          </div>
        </div>

        <div className="booking-detail">
          <span>🕐</span>
          <div>
            <small>Time</small>
            <p>{booking.time}</p>
          </div>
        </div>

        <div className="booking-detail">
          <span>👥</span>
          <div>
            <small>Guests</small>
            <p>{booking.guests} People</p>
          </div>
        </div>
      </div>

      <div className="booking-card-footer">
        <span className="booking-id">
          Booking ID: #{String(booking.id).padStart(4, "0")}
        </span>

        {active ? (
          <button className="cancel-btn">Cancel Booking</button>
        ) : (
          <button className="details-btn">View Details</button>
        )}
      </div>
    </div>
  );
}

export default function Bookings() {
  return (
    <main className="bookings-page">
      <div className="bookings-container">
        <div className="bookings-heading">
          <div>
            <span className="page-subtitle">RESERVATIONS</span>
            <h1>My Bookings</h1>
            <p>Manage your upcoming and previous restaurant reservations.</p>
          </div>
        </div>

        {/* Active Bookings */}
        <section className="booking-section">
          <div className="section-heading">
            <h2>Active Bookings</h2>
            <span className="booking-count">
              {activeBookings.length} Active
            </span>
          </div>

          <div className="bookings-grid">
            {activeBookings.length > 0 ? (
              activeBookings.map((booking) => (
                <BookingCard
                  key={booking.id}
                  booking={booking}
                  active={true}
                />
              ))
            ) : (
              <p className="empty-bookings">
                You don't have any active bookings.
              </p>
            )}
          </div>
        </section>

        {/* Previous Bookings */}
        <section className="booking-section previous-section">
          <div className="section-heading">
            <h2>Previous Bookings</h2>
            <span className="booking-count">
              {previousBookings.length} Bookings
            </span>
          </div>

          <div className="bookings-grid">
            {previousBookings.length > 0 ? (
              previousBookings.map((booking) => (
                <BookingCard
                  key={booking.id}
                  booking={booking}
                  active={false}
                />
              ))
            ) : (
              <p className="empty-bookings">
                You don't have any previous bookings.
              </p>
            )}
          </div>
        </section>
      </div>
    </main>
  );
}