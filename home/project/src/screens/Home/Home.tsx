import { HomeIcon, SettingsIcon } from "lucide-react";
import React from "react";
import { Card, CardContent } from "../../components/ui/card";
import { Separator } from "../../components/ui/separator";

export const Home = (): JSX.Element => {
  return (
    <div className="bg-[#154734] flex flex-col min-h-screen w-full">
      <header className="w-full flex justify-between items-center">
        <div className="w-[300px] h-[200px] bg-[url(/logo.svg)] bg-[100%_100%]">
          <HomeIcon className="h-12 w-12 text-white m-6" />
        </div>
        <div className="w-[300px] h-[200px] flex justify-end items-start">
          <SettingsIcon className="h-12 w-12 text-white m-6" />
        </div>
      </header>

      <main className="flex-1 border border-black">
        <Card className="h-[662px] flex items-center justify-center">
          <CardContent className="flex flex-col items-center justify-center">
            <h1 className="font-serif text-white text-[64px]">My Clubs</h1>
            <Separator className="w-[500px] h-[5px] mt-4 bg-white" />
          </CardContent>
        </Card>
      </main>

      <footer className="grid grid-cols-2 h-[281px]">
        <Card className="rounded-none">
          <CardContent className="flex flex-col items-center justify-center h-full">
            <h2 className="font-serif text-white text-[64px]">Join Clubs</h2>
            <Separator className="w-[500px] h-[5px] mt-4 bg-white" />
          </CardContent>
        </Card>

        <Card className="rounded-none">
          <CardContent className="flex flex-col items-center justify-center h-full">
            <h2 className="font-serif text-white text-[64px]">Manage Clubs</h2>
            <Separator className="w-[500px] h-[5px] mt-4 bg-white" />
          </CardContent>
        </Card>
      </footer>
    </div>
  );
};
