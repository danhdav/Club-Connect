import React from "react";
import {
  HomeIcon,
  SettingsIcon,
  User,
  LogOut,
  Lock,
  Trash2
} from "lucide-react";
import { Card, CardContent } from "../../components/ui/card";
import { Separator } from "../../components/ui/separator";
import { Link } from "react-router-dom";

export const Settings = (): JSX.Element => {
  // demo settings actions
  const actions = [
    { id: 1, title: "Change Username", Icon: User },
    { id: 2, title: "Log Out", Icon: LogOut },
    { id: 3, title: "Change Password", Icon: Lock },
    { id: 4, title: "Delete Account", Icon: Trash2 },
  ];

  return (
    <div className="bg-[#154734] flex flex-col min-h-screen w-full text-white">
      {/* Header: same layout as Home */}
      <header className="flex justify-between items-center px-8 py-4 bg-[#154734]">
        <Link
          to="/home"
          className="flex items-center space-x-2 text-white hover:text-gray-200 transition"
        >
          <HomeIcon size={32} />
          <span className="text-2xl font-semibold">Home</span>
        </Link>

        <div className="flex items-center space-x-2">
          <SettingsIcon size={32} />
          <span className="text-2xl font-semibold">Settings</span>
        </div>
      </header>

      <Separator className="border-[#e87500]" />

      <main className="container mx-auto px-8 py-12 flex flex-col gap-8">
        {actions.map(({ id, title, Icon }) => (
          <Card
            key={id}
            onClick={() => alert(title)}
            className="bg-white text-[#154734] hover:shadow-xl transition p-6 rounded-lg border-2 border-[#e87500] cursor-pointer"
          >
            <CardContent className="flex flex-col items-center">
              <Icon size={48} className="mb-4" />
              <h2 className="text-3xl font-bold">{title}</h2>
            </CardContent>
          </Card>
        ))}
      </main>
    </div>
  );
};
