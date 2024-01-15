import { useKeycloak } from "@react-keycloak/web";

const MyComponent = () => {
    const { keycloak, initialized } = useKeycloak();

    if (!initialized) {
        return <div>Loading...</div>;
    }

    return keycloak.authenticated? (
        <div>
            Welcome back, {keycloak.tokenParsed?.preferred_username}!
        </div>
        ) : (
        <div>
            <button onClick={() => keycloak.login()}>Login</button>
        </div>
    );
};

export default MyComponent;