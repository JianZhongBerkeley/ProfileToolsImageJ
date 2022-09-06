# this example python script demonstrates performing FFT on ImageJ profiles in Profile Tools

def pt_single_plot_to_single_plot(xs, ys, fs : float = 1000.0):
    """ Scipy fft of ImageJ profile
    Parameters:
        xs: [1D numpy array] input profile X-axis data
        ys: [1D numpy array] input profile Y-axis data
        fs: [float] sampling frequency input from Profile Tools
    Return:
        (xfs, yfs)
        xfs: [1D numpy array] output profile X-axis data (frequencies of the FFT result)
        yfs: [1D numpy array] output profile Y-axis data (FFT result)
    """
    from scipy.fft import fft, fftfreq
    import numpy as np
    N = ys.size
    T = 1.0 / fs
    yfs = fft(ys)
    xfs = fftfreq(N, T)
    return (xfs[:N//2], (2.0/N) * np.abs(yfs)[0:N//2])