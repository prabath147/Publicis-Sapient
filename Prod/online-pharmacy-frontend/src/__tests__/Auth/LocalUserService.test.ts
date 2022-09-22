import { User } from "../../components/pages/Auth/Login/models";
import LocalUserService from "../../services/LocalUserService";

it("testing Local User service", () => {
  const user: User = {
    id: 1,
    email: "user1@email.com",
    role: "ROLE_ADMIN",
    username: "user1",
  };

  LocalUserService.removeLocalUser();
  expect(LocalUserService.getLocalUser()).toBeNull();

  LocalUserService.setLocalUser(user);
  expect(LocalUserService.getLocalUser()).toStrictEqual(user);

  LocalUserService.removeLocalUser();
});
