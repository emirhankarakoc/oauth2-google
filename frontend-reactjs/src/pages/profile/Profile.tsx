import { APIURL, http, httpError } from "@/assets/http";
import Navigation from "@/components/Navigation";
import React, { useEffect, useState } from "react";
import { useGoogleLogin } from "@react-oauth/google";
import axios from "axios";
import { Button } from "@nextui-org/button";

export default function Profile() {
  // Define a type for user state
  type User = {
    id: string;
    email: string;
    role: string;
    oauth2: OAuth2Account;
    userStatus: UserStatus; // Include userStatus
  } | null;

  type UserStatus = "GOOGLE_VERIFICATED" | "GOOGLE_NOT_VERIFICATED"; // Define possible statuses

  interface OAuth2Account {
    id: string;
    email: string;
    family_name: string;
    given_name: string;
    gmailId: string;
    name: string;
    picture: string;
    verified_email: boolean;
    gmailAccessToken: string;
  }

  const [user2, setUser2] = useState<OAuth2Account>();

  const login = useGoogleLogin({
    onSuccess: (codeResponse) => {
      setUser2(codeResponse);
    },
    onError: (error) => console.log("Login Failed:", error),
  });

  useEffect(() => {
    const fetchUserDetails = async () => {
      if (user2) {
        try {
          const response = await axios.get(
            `https://www.googleapis.com/oauth2/v1/userinfo?access_token=${user2.access_token}`,
            {
              headers: {
                Authorization: `Bearer ${user2.gmailAccessToken}`,
                Accept: "application/json",
              },
            }
          );
          console.log(response.data);
          //user geldi.
          //datalari backende yollama zamani ve kaydolma zamani.
          const userprofile = await axios.post(
            `${APIURL}/accounts/oauth2/${user2.gmailAccessToken}`,
            response.data,
            {
              headers: {
                Authorization: `Bearer ${localStorage.getItem("jwtToken")}`,
                "Content-Type": "application/json",
              },
            }
          );
          console.log(userprofile);

          console.log("ikinci istegi de yolladim");
        } catch (err) {
          httpError(err);
        }
      }
    };

    fetchUserDetails();
  }, [user2]);

  // Initialize state as User or null
  const [user, setUser] = useState<User>(null);

  useEffect(() => {
    const handleGetMe = async () => {
      try {
        const response = await http.get(`${APIURL}/accounts/getme`);
        console.log(response.data);
        setUser(response.data);
      } catch (error) {
        console.log(error);
        setUser(null); // Correctly set state to null on error
      }
    };

    handleGetMe();
  }, []);
  const [isLoading, setLoading] = useState(false);
  const handleLogOutOAuth2Account = async () => {
    setLoading(true);
    //
    try {
      await http.post(`/accounts/delete/oauth2connection/${user?.id}`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("jwtToken")}`,
          "Content-Type": "application/json",
        },
      });
    } catch (errr) {
      httpError(errr);
    }
    setLoading(false);
  };
  return (
    <div>
      <div>
        <Navigation />
      </div>
      <div>
        {user ? (
          <div className="grid place-items-center h-screen ">
            <div className="">
              <div className="font-semibold">User Information</div>
              <p></p>
              UserId: {user.id} --saved to localstorage
              <p></p>
              Email: {user.email}
              <p></p>
              Role: {user.role}
              <p></p>
              <div className="font-semibold">
                Also saved JWT token to header in http.tsx
              </div>
              {user.userStatus == "GOOGLE_NOT_VERIFICATED" ? (
                <div>
                  <div className="my-3 bg-blue-500 grid place-items-center rounded-3xl p-10 hover:bg-blue-600 hover:text-white">
                    <button onClick={() => login()}>
                      Connect account with Google 🚀
                    </button>
                  </div>
                </div>
              ) : (
                <div>
                  <div className="mt-15 border-3 border-green-600  text-3xl flex flex-col">
                    <p>Kisi dogrulanmistir.</p>
                    <div className="font-sfpro">
                      GMAIL ADRESI:{user.oauth2.email}
                    </div>
                  </div>
                  <Button
                    isLoading={isLoading}
                    className="mt-20"
                    color="danger"
                    onClick={handleLogOutOAuth2Account}
                  >
                    GMAIL HESABIYLA BAGLANTIYI KES
                  </Button>
                </div>
              )}
            </div>
          </div>
        ) : (
          <div className="grid place-items-center h-screen ">
            <p className="font-semibold">Login first</p>
          </div>
        )}
      </div>
      <div></div>
    </div>
  );
}
