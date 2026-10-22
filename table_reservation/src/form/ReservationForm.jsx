import "./reservationForm.css";

function ReservationForm() {
  const handleSubmit = (e) => {
    e.preventDefault();

    // Handle reservation here
    console.log("Reservation submitted");
  };

  const table="T1";
  const tableCapacity=4;

  return (
    <div className="reservation-container">
      <form className="reservation-form" onSubmit={handleSubmit}>
        <h2>Reserve a Table</h2>
        <p className="form-subtitle">
          {`you have selected table ${table}`}
        </p>
        <p className="form-subtitle">
          Fill in the details to reserve your table
        </p>

        <div className="form-group">
          <label htmlFor="name">Name</label>
          <input
            type="text"
            id="name"
            name="name"
            placeholder="Enter your name"
            required
          />
        </div>

        <div className="form-row">
          <div className="form-group">
            <label htmlFor="people">Number of People</label>
            <input
              type="number"
              id="people"
              name="people"
              min="1"
              max={tableCapacity}
              placeholder="2"
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="date">Date</label>
            <input type="date" id="date" name="date" required />
          </div>
        </div>

        <div className="form-row">
          <div className="form-group">
            <label htmlFor="time">Reservation Time</label>
            <input type="time" id="time" name="time" required />
          </div>

          <div className="form-group">
            <label htmlFor="phone">Phone Number</label>
            <input
              type="tel"
              id="phone"
              name="phone"
              placeholder="Enter phone number"
              required
            />
          </div>
        </div>

        <div className="form-group">
          <label htmlFor="notes">Special Requests</label>
          <textarea
            id="notes"
            name="notes"
            placeholder="Any special requests?"
            rows="3"
          ></textarea>
        </div>

        <button type="submit" className="reserve-btn">
          Reserve Table
        </button>
      </form>
    </div>
  );
}

export default ReservationForm;
