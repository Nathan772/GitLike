import DarkModeSwitch from '../components/DarkModeSwitch';
import InputRepository from '../components/InputRepository';
import History from '../components/History';

function Home() {
    return (
        <>
            <main class="container min-vh-100 d-flex flex-column justify-content-center align-items-center py-5">
                <h1 class="mb-3">GitClout</h1>

                <InputRepository />

                <div class="mt-5 w-50">
                    <History />

                </div>

                <div class="position-fixed bottom-0 end-0 p-3">
                    <DarkModeSwitch />
                </div>
            </main>
        </>
    )
}

export default Home