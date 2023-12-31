// ReserveButton.tsx
import React from 'react';
import styles from "./ReservationButton.module.css"; // CSS 모듈 가져오기

interface ReservationButtonProps {
  onClick?: () => void;
  buttonTitle?: string;
}

const ReservationButton: React.FC<ReservationButtonProps> = ({ onClick, buttonTitle }) => {
  return (
    <div className={styles.reservationButtonContainer}>
      <button
        className={styles.reservationButton} // CSS 모듈 클래스 적용
        onClick={onClick}
        type='submit'
      >
        {buttonTitle ?? "예약 하기"}
      </button>
    </div>
  );
};

export default ReservationButton;
