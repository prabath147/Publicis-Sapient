class TokenService {
  getLocalRefreshToken() {
    const refresh_token = localStorage.getItem("refresh_token");
    return refresh_token === null ? "" : refresh_token;
  }
  getLocalAccessToken() {
    const access_token = localStorage.getItem("access_token");
    return access_token === null ? "" : access_token;
  }
  updateLocalAccessToken(access_token: string) {
    localStorage.setItem("access_token", access_token);
  }
  updateLocalRefreshToken(refresh_token: string) {
    localStorage.setItem("refresh_token", refresh_token);
  }
  removeLocalAccessToken() {
    localStorage.removeItem("access_token");
  }
  removeLocalRefreshToken() {
    localStorage.removeItem("refresh_token");
  }
}
export default new TokenService();
