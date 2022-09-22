import "@testing-library/jest-dom";
import { fireEvent, render, screen } from '@testing-library/react';
import { act } from 'react-dom/test-utils';
import { Provider } from 'react-redux';
import { BrowserRouter } from 'react-router-dom';
import renderer from 'react-test-renderer';
import { store } from '../../app/store';
import Login from '../../components/pages/Auth/Login/Login';
import { LoginRequest, LoginResponse, User } from '../../components/pages/Auth/Login/models';
import mockAxios from "../../__mocks__/axios";

// setup
const onSubmit = jest.fn();

const MockLogin = () => {
  return (
    <Provider store={store}>
      <BrowserRouter>
        <Login onSubmit={onSubmit} />
      </BrowserRouter>
    </Provider>
  )
}


const loginOutput: LoginResponse = {
  email: "one@email.com",
  id: 1,
  username: "1234567890",
  refreshToken: "t1",
  token: "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwicm9sZSI6IlJPTEVfQURNSU4iLCJleHAiOjE1MTY5MzkwMjIsImlhdCI6MTUxNjIzOTAyMn0.lN42btTYcPU_3yBUC0Qh6qjWzpfagp3Av5MABEA15Y8"
}


it("component created", () => {
  const component = renderer.create(<MockLogin />);
  const tree = component.toJSON();
  expect(tree).toMatchSnapshot();
});

const LoginInput: LoginRequest = {
  username: "ankit1",
  password: "1234",
};

describe("Login tests", () => {

  afterEach(() => {
    jest.resetAllMocks();
  });

  it("Check if form is rendered correctly", () => {
    render(<MockLogin />);

    expect(
      screen.getByRole("textbox", { name: /User Name/i })
    ).toBeInTheDocument();
    expect(screen.getByLabelText(/password/i)).toBeInTheDocument();
    expect(
      screen.getByRole("button", { name: /Sign in/i })
    ).toBeInTheDocument();
  });

  it("onSubmit should be called when the user inputs valid details", async () => {
    mockAxios.post.mockImplementationOnce(() =>
      Promise.resolve({ data: loginOutput })
    );

    const { getByLabelText, getByRole } = render(<MockLogin />);

    await act(async () => {
      fireEvent.change(getByRole("textbox", { name: /User Name/i }), {
        target: { value: "1" },
      });
      fireEvent.change(getByLabelText(/password/i), {
        target: { value: "1234" },
      });
    });

    await act(async () => {
      fireEvent.click(getByRole("button", { name: /Sign in/i }));
    });

    await act(async () => {
      fireEvent.change(getByRole("textbox", { name: /User Name/i }), {
        target: { value: "ankit1" },
      });
      fireEvent.change(getByLabelText(/password/i), {
        target: { value: "1234" },
      });
    });

    await act(async () => {
      fireEvent.click(getByRole("button", { name: /Sign in/i }));
    });

    expect(onSubmit).toBeCalledWith(LoginInput);
  });

  it("should handle errors", async () => {
    mockAxios.post.mockImplementationOnce(() => Promise.resolve());

    const { getByLabelText, getByRole } = render(<MockLogin />);

    await act(async () => {
      fireEvent.change(getByRole("textbox", { name: /User Name/i }), {
        target: { value: "ankit1" },
      });
      fireEvent.change(getByLabelText(/password/i), {
        target: { value: "1234" },
      });
    });

    const data: LoginRequest = {
      username: "ankit1",
      password: "1234",
    };

    await act(async () => {
      fireEvent.click(getByRole("button", { name: /Sign in/i }));
    });

    expect(onSubmit).toBeCalledWith(data);
  })

})

describe('User Slice tests', () => {
  const initialState: User = {
    id: -1,
    username: "",
    email: "",
    role: "",
  }

  const user: User = {
    email: "one@email.com",
    id: 1,
    username: "1234567890",
    role: "ROLE_ADMIN"
  }

  // TODO: update this
  // it("should handle initial state", () => {
  //     const actual: User = UserReducer(initialState, LoginAction(loginOutput))
  //     expect(actual).toEqual(user)

  //     const actual2: User = UserReducer(initialState, LogoutAction())
  //     expect(actual2).toEqual(initialState)
  // });
})


// function getUsername() {
//     return screen.getByRole('textbox', {
//         name: /username/i
//     });
// }

// function getPassword() {
//     return screen.getByLabelText(/password/i);
// }

// function clickSubmitButton() {
//     user.click(screen.getByRole('button', {
//         name: /sign in/i
//     }));
// }
