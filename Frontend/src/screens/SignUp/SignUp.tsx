import React, { useState, ChangeEvent, FormEvent } from "react";
import { Link, useNavigate } from "react-router-dom";
import axios from "axios";

import { Button } from "../../components/ui/button";
import { Card, CardContent } from "../../components/ui/card";
import { Input } from "../../components/ui/input";

export const SignUp: React.FC = () => {
  const [name, setName] = useState<string>("");
  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const [confirmPassword, setConfirmPassword] = useState<string>("");
  const [message, setMessage] = useState<string>("");
  const [isSubmitting, setIsSubmitting] = useState<boolean>(false);

  const navigate = useNavigate();

  // generic onChange helper
  const handleChange =
    (setter: React.Dispatch<React.SetStateAction<string>>) =>
    (e: ChangeEvent<HTMLInputElement>) =>
      setter(e.target.value);

  const validateForm = (): boolean => {
    // Reset message
    setMessage("");
    
    // Check for empty fields
    if (!name.trim() || !email.trim() || !password || !confirmPassword) {
      setMessage("All fields are required.");
      return false;
    }
    
    // Validate email format
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
      setMessage("Please enter a valid email address.");
      return false;
    }
    
    // Check password length
    if (password.length < 6) {
      setMessage("Password must be at least 6 characters long.");
      return false;
    }
    
    // Check if passwords match
    if (password !== confirmPassword) {
      setMessage("Passwords do not match.");
      return false;
    }
    
    return true;
  };

  const handleRegister = async (e?: FormEvent) => {
    if (e) e.preventDefault();
    
    if (!validateForm()) {
      return;
    }

    setIsSubmitting(true);

    const sanitizedName = name.replace(/[\r\n]/g, '');
    const sanitizedEmail = email.replace(/[\r\n]/g, '');
    const sanitizedPassword = password.replace(/[\r\n]/g, '');

    try {
      const response = await axios.post("http://localhost:8080/auth/register", {
        name: sanitizedName,
        email: sanitizedEmail,
        password: sanitizedPassword,
      });
      
      console.log("Registration successful:", response.data);
      
      // Navigate after successful registration
      setTimeout(() => {
        navigate("/");
      }, 100);
    } catch (error) {
      console.error("Registration error:", error);
      if (axios.isAxiosError(error) && error.response) {
        // Server returned an error response
        setMessage(error.response.data.message || "Registration failed – please check your input.");
      } else {
        setMessage("Registration failed. Please try again later.");
      }
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="flex flex-col w-full min-h-screen bg-[#e87500]">
      <div className="flex flex-1 relative overflow-hidden">
        {/* Green gradient background */}
        <div className="absolute top-0 left-0 w-3/4 h-full [background:linear-gradient(90deg,rgba(113,156,122,1)_0%,rgba(21,71,52,1)_100%)]" />
        
        <div className="flex flex-col w-full items-center justify-center relative z-10">
          <div className="flex flex-col w-full items-center">
            {/* Signup card container */}
            <div className="w-full max-w-[980px] mx-auto mt-4 mb-4 relative">
              <Card className="w-full h-[927px] bg-white border-[10px] border-solid border-[#154734] rounded-none shadow-none">
                <CardContent className="p-0 h-full relative">
                  <form onSubmit={handleRegister} className="h-full">
                    <h1 className="absolute top-3 left-[18px] [text-shadow:8px_8px_4px_#00000040] font-[Permanent_Marker] text-black text-8xl">
                      Club&nbsp;Connect
                    </h1>

                    {/* Name label */}
                    <label className="absolute top-[130px] left-[18px] font-[Source_Serif_Pro] text-black text-5xl">
                      Name:
                    </label>

                    {/* Name input */}
                    <div className="absolute w-[776px] h-[102px] top-[190px] left-[73px] flex items-center">
                      <div className="h-full w-[10px] bg-[#e87500]" />
                      <Input
                        placeholder="Your full name"
                        value={name}
                        onChange={handleChange(setName)}
                        className="h-full w-full border-none rounded-none [background:linear-gradient(90deg,rgba(255,246,238,1)_0%,rgba(255,225,195,1)_100%)] focus-visible:ring-0"
                        required
                      />
                    </div>

                    {/* Email label */}
                    <label className="absolute top-[310px] left-[18px] font-[Source_Serif_Pro] text-black text-5xl">
                      Email:
                    </label>

                    {/* Email input */}
                    <div className="absolute w-[776px] h-[102px] top-[370px] left-[73px] flex items-center">
                      <div className="h-full w-[10px] bg-[#e87500]" />
                      <Input
                        type="email"
                        placeholder="you@example.com"
                        value={email}
                        onChange={handleChange(setEmail)}
                        className="h-full w-full border-none rounded-none [background:linear-gradient(90deg,rgba(255,246,238,1)_0%,rgba(255,225,195,1)_100%)] focus-visible:ring-0"
                        required
                      />
                    </div>

                    {/* Password label */}
                    <label className="absolute top-[490px] left-[18px] font-[Source_Serif_Pro] text-black text-5xl">
                      Password:
                    </label>

                    {/* Password input */}
                    <div className="absolute w-[776px] h-[102px] top-[550px] left-[73px] flex items-center">
                      <div className="h-full w-[10px] bg-[#e87500]" />
                      <Input
                        type="password"
                        placeholder="Choose a password"
                        value={password}
                        onChange={handleChange(setPassword)}
                        className="h-full w-full border-none rounded-none [background:linear-gradient(90deg,rgba(255,246,238,1)_0%,rgba(255,225,195,1)_100%)] focus-visible:ring-0"
                        required
                        minLength={6}
                      />
                    </div>

                    {/* Confirm Password label */}
                    <label className="absolute top-[670px] left-[18px] font-[Source_Serif_Pro] text-black text-5xl">
                      Confirm:
                    </label>

                    {/* Confirm Password input */}
                    <div className="absolute w-[776px] h-[102px] top-[730px] left-[73px] flex items-center">
                      <div className="h-full w-[10px] bg-[#e87500]" />
                      <Input
                        type="password"
                        placeholder="Re-type your password"
                        value={confirmPassword}
                        onChange={handleChange(setConfirmPassword)}
                        className="h-full w-full border-none rounded-none [background:linear-gradient(90deg,rgba(255,246,238,1)_0%,rgba(255,225,195,1)_100%)] focus-visible:ring-0"
                        required
                      />
                    </div>
                  </form>
                </CardContent>
              </Card>

              <div className="flex justify-between items-center mt-8 w-full px-4">
                {/* Log in link */}
                <Link
                  to="/"
                  className="font-[Source_Serif_Pro] text-[#1eb4cf] text-[32px] hover:underline"
                >
                  Already have an account? Log In
                </Link>

                {/* Register button */}
                <Button
                  className="w-[232px] h-[86px] bg-[#e87500] hover:bg-[#e87500]/90 border-[5px] border-[#154734] rounded-none font-[Source_Serif_Pro] text-5xl"
                  onClick={() => handleRegister()}
                  disabled={isSubmitting}
                >
                  {isSubmitting ? "..." : "Register"}
                </Button>
              </div>
            </div>
          </div>

          {/* Feedback message */}
          {message && (
            <div className="w-full text-center absolute bottom-10">
              <p className="text-xl text-red-600">
                {message}
              </p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};