# Example code of profile processing with single profile input and value output python script

def pt_single_plot_value(xs, ys):
    """ Caculate mean of the profile plot
    Parameters:
        xs: [1D numpy array] input profile X-axis data
        ys: [1D numpy array] input profile Y-axis data
    Return:       
        avg: [float] mean value of the profile Y-axis data (result will be posted on ImageJ log window)
    """
    sum = 0
    N = ys.size
    for i in range(N):
        sum = sum + ys[i]
    avg = (1.0 * sum)/N
    return avg