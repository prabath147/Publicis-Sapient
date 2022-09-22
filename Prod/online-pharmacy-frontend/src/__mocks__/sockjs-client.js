const mockStomp = jest.genMockFromModule("sockjs-client")

mockStomp.mockImplementation(()=>mockStomp)

export default mockStomp