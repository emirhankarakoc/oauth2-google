import { Button } from "@nextui-org/button";
import {
  Navbar,
  NavbarBrand,
  NavbarContent,
  NavbarItem,
} from "@nextui-org/navbar";
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

export default function Navigation() {
  const [isLoggedIn, setLoggedIn] = useState(false);

  useEffect(() => {
    const handleToken = () => {
      const token = localStorage.getItem("jwtToken");
      if (token != null) {
        setLoggedIn(true);
      }
    };
    handleToken();
  }, [isLoggedIn]);

  const handleLogOut = () => {
    localStorage.removeItem("jwtToken");
    setLoggedIn(false);
    window.location.href = "/";
  };

  return (
    <Navbar>
      <NavbarBrand>
        <button
          className="font-bold text-inherit"
          onClick={() => {
            window.location.href = "/";
          }}
        >
          ACME
        </button>
      </NavbarBrand>
      <NavbarContent className="hidden sm:flex gap-4" justify="center">
        <NavbarItem>
          <Link to="#" className="text-foreground">
            Features
          </Link>
        </NavbarItem>
        <NavbarItem isActive>
          <Link to="#" aria-current="page">
            Customers
          </Link>
        </NavbarItem>
        <NavbarItem>
          <Link to="#" className="text-foreground">
            Integrations
          </Link>
        </NavbarItem>
      </NavbarContent>
      {!isLoggedIn && (
        <NavbarContent justify="end">
          <NavbarItem className="hidden lg:flex">
            <Link to="/login">Login</Link>
          </NavbarItem>
          <NavbarItem>
            <Button as={Link} to="/register" color="primary" variant="flat">
              Sign Up
            </Button>
          </NavbarItem>
        </NavbarContent>
      )}
      {isLoggedIn && (
        <NavbarContent justify="end">
          <NavbarItem className="hidden lg:flex">
            <Button as={Link} to="/profile" color="secondary" variant="flat">
              Profile
            </Button>
          </NavbarItem>
          <NavbarItem>
            <Button onPress={handleLogOut} color="danger" variant="flat">
              Logout
            </Button>
          </NavbarItem>
        </NavbarContent>
      )}
    </Navbar>
  );
}
