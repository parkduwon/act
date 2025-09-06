import React, { useState, useEffect } from "react";
import { Input } from "./input";
import { formatCurrency, parseCurrency } from "@/utils/currency";
import type { Currency } from "@/utils/currency";

interface CurrencyInputProps {
  id?: string;
  value: number | undefined | null;
  onChange: (value: number) => void;
  currency: Currency;
  className?: string;
  placeholder?: string;
  disabled?: boolean;
}

export function CurrencyInput({
  id,
  value,
  onChange,
  currency,
  className = "",
  placeholder,
  disabled = false
}: CurrencyInputProps) {
  const [localValue, setLocalValue] = useState<string>("");
  const [isFocused, setIsFocused] = useState(false);

  useEffect(() => {
    if (!isFocused) {
      // 포커스가 없을 때만 포맷팅된 값 표시
      setLocalValue(formatCurrency(value, currency));
    }
  }, [value, currency, isFocused]);

  const handleFocus = () => {
    setIsFocused(true);
    // 포커스 시 숫자만 표시
    if (value !== undefined && value !== null) {
      setLocalValue(value.toString());
    } else {
      setLocalValue("");
    }
  };

  const handleBlur = () => {
    setIsFocused(false);
    let numValue = parseCurrency(localValue);
    // 소수점 5자리까지만 저장 (절사)
    numValue = Math.floor(numValue * 100000) / 100000;
    onChange(numValue);
    // 블러 시 포맷팅된 값 표시
    setLocalValue(formatCurrency(numValue, currency));
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const input = e.target.value;
    // 숫자, 소수점, 마이너스만 허용
    if (/^-?\d*\.?\d*$/.test(input) || input === "") {
      setLocalValue(input);
    }
  };

  return (
    <Input
      id={id}
      type="text"
      className={`text-right ${className}`}
      value={localValue}
      onChange={handleChange}
      onFocus={handleFocus}
      onBlur={handleBlur}
      placeholder={placeholder}
      disabled={disabled}
    />
  );
}