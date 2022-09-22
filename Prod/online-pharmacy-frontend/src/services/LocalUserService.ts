import { User } from "../components/pages/Auth/Login/models";

class LocalUserService {
  setLocalUser(user: User) {
    localStorage.setItem("user", JSON.stringify(user));
  }
  getLocalUser() {
    const user = localStorage.getItem("user");
    return user === null ? null : JSON.parse(user);
  }
  removeLocalUser() {
    localStorage.removeItem("user");
  }
}

export default new LocalUserService();
