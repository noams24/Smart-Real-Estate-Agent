"use client";

import * as React from "react";
import { Check, ChevronDown } from "lucide-react";

import { cn } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
} from "@/components/ui/command";
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover";

const cities = [
  {
    value: "תל אביב",
    label: "תל אביב",
  },
  {
    value: "חיפה",
    label: "חיפה",
  },
  {
    value: "אשדוד",
    label: "אשדוד",
  },
  {
    value: "אשקלון",
    label: "אשקלון",
  },
  {
    value: "ראשון לציון",
    label: "ראשון לציון",
  },
  {
    value: "רמת גן",
    label: "רמת גן",
  },
  {
    value: "באר שבע",
    label: "באר שבע",
  },
  {
    value: "נתניה",
    label: "נתניה",
  },
  {
    value: "פתח תקווה",
    label: "פתח תקווה",
  },
  {
    value: "ירושלים",
    label: "ירושלים",
  },

];

interface ComboboxProps {
  city: string;
  setCity: (city: string) => void;
}

export default function Combobox({ city, setCity }: ComboboxProps) {
  const [open, setOpen] = React.useState(false);
  return (
    <Popover open={open} onOpenChange={setOpen}>
      <PopoverTrigger asChild>
        <Button
          variant="outline"
          role="combobox"
          aria-expanded={open}
          className="w-[200px] justify-between"
        >
          {city
            ? cities.find((framework) => framework.value === city)?.label
            : "חפש לפי עיר..."}
          <ChevronDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
        </Button>
      </PopoverTrigger>
      <PopoverContent className="w-[200px] p-0">
        <Command>
          <CommandInput placeholder="חפש לפי עיר..." />
          <CommandEmpty>עיר לא קיימת</CommandEmpty>
          <CommandGroup>
            <CommandList>
              {cities.map((framework) => (
                <CommandItem
                  key={framework.value}
                  value={framework.value}
                  onSelect={(currentValue) => {
                    setCity(currentValue === city ? "" : currentValue);
                    setOpen(false);
                  }}
                >
                  <Check
                    className={cn(
                      "mr-2 h-4 w-4",
                      city === framework.value ? "opacity-100" : "opacity-0"
                    )}
                  />
                  {framework.label}
                </CommandItem>
              ))}
            </CommandList>
          </CommandGroup>
        </Command>
      </PopoverContent>
    </Popover>
  );
}
