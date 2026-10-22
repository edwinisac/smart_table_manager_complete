
import { useState } from "react";
import "./tableReservation.css";
import { useNavigate } from "react-router-dom";

function TableReservation() {

  const navigate=useNavigate();
  const [selectedTable, setSelectedTable] = useState(null);

  const tables = [
    { id: 1, seats: 2, status: "available", x: 15, y: 20 },
    { id: 2, seats: 4, status: "booked", x: 35, y: 20 },
    { id: 3, seats: 4, status: "available", x: 55, y: 20 },
    { id: 4, seats: 6, status: "available", x: 75, y: 20 },

    { id: 5, seats: 2, status: "available", x: 15, y: 50 },
    { id: 6, seats: 4, status: "booked", x: 35, y: 50 },
    { id: 7, seats: 4, status: "available", x: 55, y: 50 },
    { id: 8, seats: 6, status: "unavailable", x: 75, y: 50 },

    { id: 9, seats: 2, status: "available", x: 15, y: 80 },
    { id: 10, seats: 4, status: "available", x: 35, y: 80 },
    { id: 11, seats: 4, status: "booked", x: 55, y: 80 },
    { id: 12, seats: 6, status: "available", x: 75, y: 80 },
  ];

  const handleTableClick = (table) => {
    if (table.status !== "available") {
      return;
    }

    setSelectedTable(table.id);
    console.log(table.id);
    navigate(`/form/${table.id}`);
    
  };

  return (
    <div className="reservation-page">

      <h1>Select Your Table</h1>
      <p className="reservation-subtitle">
        Choose an available table from the restaurant layout
      </p>

      {/* Legend */}

      <div className="table-legend">

        <div className="legend-item">
          <span className="legend-box available"></span>
          Available
        </div>

        <div className="legend-item">
          <span className="legend-box selected"></span>
          Selected
        </div>

        <div className="legend-item">
          <span className="legend-box booked"></span>
          Booked
        </div>

        <div className="legend-item">
          <span className="legend-box unavailable"></span>
          Unavailable
        </div>

      </div>

      {/* Restaurant */}

      <div className="restaurant">

        <div className="restaurant-label">
          RESTAURANT
        </div>

        <div className="entrance">
          ENTRANCE
        </div>

        {/* Tables */}

        {tables.map((table) => (

          <button
            key={table.id}
            className={`table ${table.status} ${
              selectedTable === table.id ? "selected" : ""
            }`}
            style={{
              left: `${table.x}%`,
              top: `${table.y}%`,
            }}
            onClick={() => handleTableClick(table)}
            disabled={table.status !== "available"}
          >
            <span className="table-number">
              T{table.id}
            </span>

            <span className="table-seats">
              {table.seats} seats
            </span>
          </button>

        ))}

      </div>

      {/* Selected Table */}

      {selectedTable && (
        <div className="reservation-summary">

          <div>
            <span>Selected Table</span>
            <strong>Table {selectedTable}</strong>
          </div>

          <button className="reserve-btn">
            Reserve Table
          </button>

        </div>
      )}

    </div>
  );
}

export default TableReservation;

