import { render } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import TokenExpired from "../../../components/pages/Auth/ForgotPassword/TokenExpired";


it("component created", () => {
    render(
        <BrowserRouter>
            <TokenExpired />
        </BrowserRouter>
    );
    expect(screen).toMatchSnapshot();
});
