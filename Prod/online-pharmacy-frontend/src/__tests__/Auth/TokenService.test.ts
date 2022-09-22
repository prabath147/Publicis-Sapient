import TokenService from "../../services/TokenService";

it("testing Local User service", () => {
  const token = "t1";
  const refreshToken = "t2";

  TokenService.removeLocalAccessToken();
  expect(TokenService.getLocalAccessToken()).toBe("");

  TokenService.removeLocalRefreshToken();
  expect(TokenService.getLocalRefreshToken()).toBe("");

  TokenService.updateLocalAccessToken(token);
  expect(TokenService.getLocalAccessToken()).toBe(token);

  TokenService.updateLocalRefreshToken(refreshToken);
  expect(TokenService.getLocalRefreshToken()).toBe(refreshToken);

  TokenService.removeLocalAccessToken();
  TokenService.removeLocalRefreshToken();
});
