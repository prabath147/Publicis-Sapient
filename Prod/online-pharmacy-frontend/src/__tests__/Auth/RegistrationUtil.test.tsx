import {
  checkPassword,
  validateEmail,
  validatePhoneNumber,
} from "../../components/pages/Auth/Registration/RegistrationUtil";

it("check password should return true for correct format", () => {
  const actual = checkPassword("John@123890");
  expect(actual).toEqual(true);
});

it("check password should return false for incorrect format", () => {
  const actual = checkPassword("123890");
  expect(actual).toEqual(false);
});

it("validate email should return true for correct format", () => {
  const actual = validateEmail("john@gmail.com");
  expect(actual).toContain("john@gmail.com");
});

it("validate email should return false for correct format", () => {
  const actual = validateEmail("123mailcom");
  expect(actual).toBeNull;
});

it("validate phone should return true for correct format", () => {
  const actual = validatePhoneNumber(9849970353);
  expect(actual).toEqual(true);
});
