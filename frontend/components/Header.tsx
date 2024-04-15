import { FaHouseUser } from "react-icons/fa";

const Header = () => {
  return (
    <nav className="sticky top-0 z-50 flex items-center justify-between py-5 px-10 max-h-24 shadow-md bg-white">
      <div className="flex items-center gap-4 font-semibold">
        <FaHouseUser className="text-primary" size={34} />
        <p>סוכן דירות חכם</p>
      </div>
    </nav>
  );
};

export default Header;
