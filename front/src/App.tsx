import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Login from '@/components/Login'
import Settings from '@/components/Settings'
import PrivateRoute from '@/components/PrivateRoute'
import { Toaster } from '@/components/ui/sonner'

function App() {
  return (
    <div className="min-h-screen bg-gray-50 flex justify-center">
      <div className="w-full max-w-[600px] bg-white min-h-screen shadow-lg">
        <BrowserRouter>
          <Routes>
            <Route path="/" element={<Login />} />
            <Route 
              path="/settings" 
              element={
                <PrivateRoute>
                  <Settings />
                </PrivateRoute>
              } 
            />
          </Routes>
        </BrowserRouter>
        <Toaster />
      </div>
    </div>
  )
}

export default App
